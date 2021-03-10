package com.app.weatherapp.mvvm.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.app.weatherapp.mvvm.data.remote.Model

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weatherResultCurrent: Model)

    @Delete
    suspend fun deleteWeather(weatherResult: Model)

    @Query("SELECT * FROM weather_table")
    fun getAllWeather(): LiveData<List<Model>>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherFavourite(weatherResultFavourite: Favourite)

    @Query("SELECT * FROM favourite_table")
    fun getAllFavouirteData(): LiveData<List<Favourite>>

    @Delete
    suspend fun deleteWeatherFavourite(weatherResult: Favourite)
}
