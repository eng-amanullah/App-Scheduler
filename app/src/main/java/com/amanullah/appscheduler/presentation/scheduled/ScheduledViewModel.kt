package com.amanullah.appscheduler.presentation.scheduled

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amanullah.appscheduler.base.scheduler.AlarmScheduler
import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.domain.DeleteAlarmUseCase
import com.amanullah.appscheduler.domain.GetAllScheduledAlarmUseCase
import com.amanullah.appscheduler.domain.InsertOrUpdateAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduledViewModel @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val getAllScheduledAlarmUseCase: GetAllScheduledAlarmUseCase,
    private val insertOrUpdateAlarmUseCase: InsertOrUpdateAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) : ViewModel() {
    val scheduledAlarm = mutableStateOf<List<Alarm>>(value = emptyList())

    fun fetchScheduledAlarms() {
        viewModelScope.launch {
            scheduledAlarm.value = getAllScheduledAlarmUseCase()
        }
    }

    fun insertOrUpdateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            insertOrUpdateAlarmUseCase(alarm = alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarm = alarm)
        }
    }

    fun editSchedule(alarm: Alarm, time: Long) {
        alarmScheduler.editSchedule(
            packageName = alarm.appName,
            newTimeInMillis = time,
            requestCode = alarm.requestCode
        )
    }

    fun cancelSchedule(requestCode: Int) {
        alarmScheduler.cancelSchedule(requestCode = requestCode)
    }
}