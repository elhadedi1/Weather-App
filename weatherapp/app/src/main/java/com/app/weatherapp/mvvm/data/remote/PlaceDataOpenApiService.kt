package com.app.weatherapp.mvvm.data.remote

import com.app.weatherapp.mvvm.data.remote.PlaceReponseOneApi.PlaceResponseOneModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceDataOpenApiService {
    @GET("direct")
    fun getPlaceDataFromOpenApi(
          @Query("q")citName:String,
          @Query("appid")key:String
    ): Call<List<PlaceResponseOneModel>>
}