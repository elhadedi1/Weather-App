package com.app.weatherapp.mvvm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.weatherapp.mvvm.data.remote.Model
import com.app.weatherapp.mvvm.data.remote.Model2

@Database(entities = arrayOf(WeatherDatabase::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CurrentWeatherDatabas : RoomDatabase() {
    abstract fun weatherDao(): CurrentWeatherDao
    // abstract fun favoriteDao(): FavoriteDao
    companion object {
        private  var instance: CurrentWeatherDatabas?=null
        @Synchronized
        fun getInstance(context: Context): CurrentWeatherDatabas {
            instance
                ?: synchronized(this){
                    instance =  Room.databaseBuilder(
                        context,
                        CurrentWeatherDatabas::class.java, "weather_database"
                    ).build()
                }
            return instance!!
        }
    }
}
