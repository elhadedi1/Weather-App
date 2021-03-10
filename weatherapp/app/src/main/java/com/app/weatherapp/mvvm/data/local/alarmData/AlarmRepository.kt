package com.app.weatherapp.data.local.alarmData

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAll()

    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }
}