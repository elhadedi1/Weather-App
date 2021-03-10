package com.app.weatherapp.mvvm.data.remote

import com.app.weatherapp.mvvm.data.remote.PlaceDataOpenApiService
import com.app.weatherapp.mvvm.data.remote.PlaceReponseOneApi.PlaceResponseOneModel
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*

object RetrofitClient {

   private val api = Retrofit.Builder()
       .baseUrl(const.API_LINK)
       .addConverterFactory(GsonConverterFactory.create())
       .build()
       .create(ApiService::class.java)

    fun getDataFromGps(
        latitude: String,
        longitude: String,
        languageCode:String,
        units:String
    ): Call<Model> {
        return api.getWeather(lati =latitude,lng=longitude, exclude = "minutely",languageCode = languageCode,units = units,appid = const.API2_KEY)
    }

   private val api2= Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/geo/1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
       .create(PlaceDataOpenApiService::class.java)
    fun getPlaceOpenApiService(city:String):Call<List<PlaceResponseOneModel>>
    {
        return api2.getPlaceDataFromOpenApi(citName = city,key = const.API2_KEY)
    }
    }


