package com.amanullah.appscheduler.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = false) val requestCode: Int,
    val appName: String,
    val time: String,
    val completed: Boolean = false
)