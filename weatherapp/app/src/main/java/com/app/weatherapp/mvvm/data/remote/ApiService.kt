package com.app.weatherapp.mvvm.data.remote



import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    fun getWeather(@Query("lat")lati:String, @Query("lon")lng:String, @Query("exclude")exclude:String, @Query("units")units:String="metric", @Query("lang")languageCode:String="en", @Query("appid")appid:String): Call<Model>
}
