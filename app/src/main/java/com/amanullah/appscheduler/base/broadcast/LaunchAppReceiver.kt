package com.amanullah.appscheduler.base.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.amanullah.appscheduler.base.utils.logger.Logger
import com.amanullah.appscheduler.data.local.db.AppDatabase
import com.amanullah.appscheduler.data.local.entity.Alarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchAppReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "LaunchAppReceiver"
    }

    // Inject AppDatabase and AlarmDao using Hilt
    @Inject
    lateinit var database: AppDatabase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == null || intent.action != "com.amanullah.appscheduler.LAUNCH_APP") {
            Logger.e(tag = TAG, message = "Received null intent or wrong action")
            return
        }

        val appName = intent.getStringExtra("PACKAGE_NAME")
        val alarmId = intent.getStringExtra("ALARM_ID")
        if (appName == null || alarmId == null) {
            Logger.e(tag = TAG, message = "Package name or alarmId is null")
            return
        }

        val packageName: String? = try {
            val packageManager = context.packageManager
            val apps = packageManager.getInstalledApplications(0)
            val appInfo =
                apps.firstOrNull { packageManager.getApplicationLabel(it).toString() == appName }
            appInfo?.packageName
        } catch (e: Exception) {
            null
        }

        // Use the injected AlarmDao
        val alarmDao = database.AlarmDao()

        // Check if alarmId is not empty
        if (alarmId.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                // Query for the alarm by alarmId (using both packageName and alarmId if needed)
                val oldAlarm = alarmDao.getAlarmById(alarmId.toInt())

                oldAlarm?.let {
                    // Create a new alarm with updated values
                    val newAlarm = Alarm(
                        requestCode = alarmId.toInt(),
                        appName = oldAlarm.appName,
                        time = oldAlarm.time,
                        completed = true
                    )

                    // Insert or update the alarm in the database
                    alarmDao.insertOrUpdateAlarm(newAlarm)
                }
            }
        }

        val launchIntent = packageName?.let { context.packageManager.getLaunchIntentForPackage(it) }
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            Logger.d(tag = TAG, message = "App launched: $packageName")
        } else {
            Logger.e(tag = TAG, message = "Failed to get launch intent for package: $packageName")
        }
    }
}