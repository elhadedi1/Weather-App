package com.app.weatherapp.mvvm.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.weatherapp.mvvm.data.remote.Model
import com.app.weatherapp.mvvm.data.remote.Model2

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weatherResultCurrent: Model2)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResult: Model)

    @Delete
    suspend fun deleteWeather(weatherResult: Model)

    @Query("SELECT * FROM model")
    fun getAllWeather(): LiveData<List<Model>>

    @Query("SELECT * FROM Model2")
    fun getAllCurrent(): LiveData<List<Model2>>
}

