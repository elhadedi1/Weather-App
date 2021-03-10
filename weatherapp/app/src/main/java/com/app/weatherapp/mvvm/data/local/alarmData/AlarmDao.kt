package com.app.weatherapp.data.local.alarmData

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm_table ORDER BY id")
    fun getAll(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarm_table WHERE id IN (:alarmIds)")
    fun loadAllByIds(alarmIds: IntArray): List<Alarm>

    @Query("SELECT * FROM alarm_table WHERE  id = :id")
    fun findById(id: Int): Alarm

    @Insert
    suspend fun insertAll(vararg alarms: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    suspend fun deleteAll()
}