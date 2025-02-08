package com.amanullah.appscheduler.domain

import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.data.repository.AlarmRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(private val repository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) {
        return repository.deleteAlarm(alarm = alarm)
    }
}