package com.amanullah.appscheduler.data.rempsitoryimpl

import com.amanullah.appscheduler.data.local.dao.AlarmDao
import com.amanullah.appscheduler.data.local.entity.Alarm
import com.amanullah.appscheduler.data.repository.AlarmRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(private val alarmDAO: AlarmDao) : AlarmRepository {
    override suspend fun insertOrUpdateAlarm(alarm: Alarm) {
        alarmDAO.insertOrUpdateAlarm(alarm = alarm)
    }

    override suspend fun getAlarmById(requestCode: Int): Alarm? {
        return alarmDAO.getAlarmById(requestCode = requestCode)
    }

    override suspend fun getAlarmByTime(time: String): Alarm? {
        return alarmDAO.getAlarmByTime(time = time)
    }

    override suspend fun getAllScheduledAlarms(): MutableList<Alarm> {
        return alarmDAO.getAllScheduledAlarms()
    }

    override suspend fun getAllCompletedAlarms(): MutableList<Alarm> {
        return alarmDAO.getCompletedAlarms()
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDAO.deleteAlarm(alarm = alarm)
    }
}