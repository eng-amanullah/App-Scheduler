package com.amanullah.appscheduler.base.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.amanullah.appscheduler.base.broadcast.LaunchAppReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmScheduler @Inject constructor(@ApplicationContext private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleApp(packageName: String, timeInMillis: Long, requestCode: Int) {
        val intent = Intent(context, LaunchAppReceiver::class.java).apply {
            action = "com.amanullah.appscheduler.LAUNCH_APP"
            putExtra("PACKAGE_NAME", packageName)
            putExtra("ALARM_ID", requestCode.toString())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    fun cancelSchedule(requestCode: Int) {
        val intent = Intent(context, LaunchAppReceiver::class.java).apply {
            action = "com.amanullah.appscheduler.LAUNCH_APP"
            putExtra("ALARM_ID", requestCode.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun editSchedule(packageName: String, newTimeInMillis: Long, requestCode: Int) {
        // Cancel the previous alarm and schedule a new one with the updated time
        cancelSchedule(requestCode)
        scheduleApp(packageName, newTimeInMillis, requestCode)
    }

    fun getUniqueRequestCode(): Int {
        return System.currentTimeMillis().toInt()
    }
}