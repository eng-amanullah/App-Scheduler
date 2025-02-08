package com.amanullah.appscheduler.presentation.apps

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amanullah.appscheduler.base.scheduler.AlarmScheduler
import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.domain.GetAlarmByTimeUseCase
import com.amanullah.appscheduler.domain.InsertOrUpdateAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    application: Application,
    private val alarmScheduler: AlarmScheduler,
    private val packageManager: PackageManager,
    private val insertOrUpdateAlarmUseCase: InsertOrUpdateAlarmUseCase,
    private val getAlarmByTimeUseCase: GetAlarmByTimeUseCase
) : AndroidViewModel(application) {
    fun getApps(): List<String> {
        // Fetching the installed apps from the packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        // Extracting app names from the installed apps list
        return installedApps.map { it.loadLabel(packageManager).toString() }.sorted()
    }

    private val _alarmByTime = MutableStateFlow<Alarm?>(value = null)
    val alarmByTime: StateFlow<Alarm?> = _alarmByTime
    fun getAlarmByTime(time: String) {
        viewModelScope.launch {
            val alarm = getAlarmByTimeUseCase(time) // Fetch the alarm from the use case
            _alarmByTime.value = alarm // Update the state with the fetched alarm
        }
    }

    fun insertOrUpdateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            insertOrUpdateAlarmUseCase(alarm = alarm)
        }
    }

    fun editSchedule(alarm: Alarm, time: Long) {
        alarmScheduler.editSchedule(
            packageName = alarm.appName,
            newTimeInMillis = time,
            requestCode = alarm.requestCode
        )
    }

    fun scheduleApp(alarm: Alarm, time: Long) {
        alarmScheduler.scheduleApp(
            packageName = alarm.appName,
            timeInMillis = time,
            requestCode = alarm.requestCode
        )
    }

    fun getUniqueRequestCode(): Int {
        return alarmScheduler.getUniqueRequestCode()
    }
}