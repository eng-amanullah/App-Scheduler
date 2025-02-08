package com.amanullah.appscheduler.domain

import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.data.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmByTimeUseCase @Inject constructor(private val repository: AlarmRepository) {
    suspend operator fun invoke(time: String): Alarm? {
        return repository.getAlarmByTime(time = time)
    }
}