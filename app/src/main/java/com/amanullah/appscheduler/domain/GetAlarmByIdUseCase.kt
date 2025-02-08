package com.amanullah.appscheduler.domain

import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.data.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmByIdUseCase @Inject constructor(private val repository: AlarmRepository) {
    suspend operator fun invoke(requestCode: Int): Alarm? {
        return repository.getAlarmById(requestCode = requestCode)
    }
}