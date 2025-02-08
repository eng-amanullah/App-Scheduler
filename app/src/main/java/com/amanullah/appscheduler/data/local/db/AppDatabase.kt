package com.amanullah.appscheduler.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amanullah.appscheduler.data.local.dao.AlarmDao
import com.amanullah.appscheduler.data.local.entity.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AlarmDao(): AlarmDao
}