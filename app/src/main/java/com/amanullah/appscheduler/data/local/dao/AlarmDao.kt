package com.amanullah.appscheduler.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amanullah.appscheduler.data.local.entity.Alarm

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE requestCode = :requestCode")
    suspend fun getAlarmById(requestCode: Int): Alarm?

    @Query("SELECT * FROM alarms WHERE time = :time")
    suspend fun getAlarmByTime(time: String): Alarm?

    // Get alarms that are not completed (completed = false)
    @Query("SELECT * FROM alarms WHERE completed = 0 ORDER BY time ASC")
    suspend fun getAllScheduledAlarms(): MutableList<Alarm>

    // Get alarms that are completed (completed = true)
    @Query("SELECT * FROM alarms WHERE completed = 1 ORDER BY time ASC")
    suspend fun getCompletedAlarms(): MutableList<Alarm>

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}