package com.amanullah.appscheduler.data.repository

import com.amanullah.appscheduler.data.local.entity.Alarm

interface AlarmRepository {
    suspend fun insertOrUpdateAlarm(alarm: Alarm)
    suspend fun getAlarmById(requestCode: Int): Alarm?
    suspend fun getAlarmByTime(time: String): Alarm?
    suspend fun getAllScheduledAlarms(): MutableList<Alarm>
    suspend fun getAllCompletedAlarms(): MutableList<Alarm>
    suspend fun deleteAlarm(alarm: Alarm)
}