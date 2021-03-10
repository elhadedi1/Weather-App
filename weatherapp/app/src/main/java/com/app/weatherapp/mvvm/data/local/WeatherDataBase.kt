package com.app.weatherapp.mvvm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.app.weatherapp.mvvm.data.remote.Model


@Database(entities = [Model::class, Favourite::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CurrentWeatherDatabas : RoomDatabase() {
    abstract fun weatherDao(): CurrentWeatherDao
    // abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
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
