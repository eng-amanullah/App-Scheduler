package com.amanullah.appscheduler.base.di

import android.content.Context
import android.content.pm.PackageManager
import androidx.room.Room
import com.amanullah.appscheduler.data.local.dao.AlarmDao
import com.amanullah.appscheduler.data.local.db.AppDatabase
import com.amanullah.appscheduler.data.rempsitoryimpl.AlarmRepositoryImpl
import com.amanullah.appscheduler.data.repository.AlarmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun providePackageManager(context: Context): PackageManager = context.packageManager

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        klass = AppDatabase::class.java,
        name = "alarms_database"
    ).build()

    @Provides
    fun provideAlarmDao(database: AppDatabase): AlarmDao = database.AlarmDao()

    @Provides
    fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository =
        AlarmRepositoryImpl(alarmDAO = alarmDao)
}
