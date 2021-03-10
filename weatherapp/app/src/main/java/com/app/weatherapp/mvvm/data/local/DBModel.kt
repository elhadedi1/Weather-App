package com.app.weatherapp.mvvm.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "weather_table")
data class WeatherDatabase(
        @PrimaryKey(autoGenerate = false)
        val id: Int = 0,
        @ColumnInfo
        val date: Int,
        val tempture: Double,
        val pressure: Int,
        val humidity: Int,
        val clouds: Int,
        val wind_speed: Double,
        val icon: String,
        val descrption: String,
        val city: String,
        val hour_Weather: List<HoursWeather>,
        val dail_Weather: List<DaysWeather>
)

data class HoursWeather(
        val date: Int,
        val tempture: Double,
        val icon: String
)

data class DaysWeather(
        val date: Int,
        val minTemp: Double,
        val maxTemp: Double,
        val icon: String,
        val sunrise:Int,
        val descrption: String
)
