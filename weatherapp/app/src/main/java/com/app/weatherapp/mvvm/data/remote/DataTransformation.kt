package com.app.weatherapp.mvvm.data.remote

import java.text.SimpleDateFormat
import java.util.*

object DataTransformation {

    fun getImage(icon:String):String{
        return "http://openweathermap.org/img/wn/${icon}.png"
    }

    fun Double.kelvinToCelsius():Int {

        return  (this - 273.15).toInt()
    }
    fun Double.celsuisToKelvin():Int{
        return (this+273.15).toInt()
    }
    fun Double.celsuisToFahrenheit():Int
    {
        return (this*33.8).toInt()
    }

    fun Double.toMilePerHour():Double{
        return (this*2.24)
    }
    fun Double.toMetersPerSeconds():Double{
        return (this/2.24)
    }

    val dateNow:String
        get(){
            val dateFormat=SimpleDateFormat("EEE, dd/MM/yyyy hh:mm a")
            val date=Date()
            return dateFormat.format(date)

        }
    val dateNow2:String
        get(){
            val dateFormat=SimpleDateFormat("hh:mm")
            val date=Date()
            return dateFormat.format(date)

        }
}