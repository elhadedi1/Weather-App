package com.app.weatherapp.mvvm.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.model.Settings
import com.app.weatherapp.mvvm.data.remote.PlaceReponseOneApi.PlaceResponseOneModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.reactivex.disposables.CompositeDisposable

import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {
    private val apiClient = RetrofitClient
    private val disposable = CompositeDisposable()
    private var getDataMutableLiveData:MutableLiveData<Model> =MutableLiveData<Model>()
    private var getPlaceMutableLiveData:MutableLiveData<PlaceResponseOneModel> =MutableLiveData<PlaceResponseOneModel>()
    fun getDataFromOpenWeather():MutableLiveData<Model>
    {
        return getDataMutableLiveData
    }
    fun getPlaceFromOpenWeather():MutableLiveData<PlaceResponseOneModel>
    {
        return getPlaceMutableLiveData
    }
    fun getPlaceFromOpenWeatherApi(city:String)
    {
        GlobalScope.launch {
            Dispatchers.IO
            try {
                val response=apiClient.getPlaceOpenApiService(city = city).execute()
                withContext(Dispatchers.Main)
                {
                    if (response.isSuccessful)
                    {
                        var myResponseAPI=response.body()!!
                        getPlaceMutableLiveData.value= myResponseAPI.get(0)
                    }
                }
            }catch (e:Exception)
            {
                Log.i("TAGTAG", "getPlaceFromApi: "+e.message)
            }
        }
    }
    fun getDataFromOpenWeatherApi(latitude: String, longitude: String)
    {
        GlobalScope.launch {
            Dispatchers.IO
            try{

                val response= apiClient.getDataFromGps(latitude, longitude, Settings.languageSystem,Settings.unitSystem).execute()


                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        var myResponseAPI=response.body()!!
                        getDataMutableLiveData.value=myResponseAPI
                    }
                }
            } catch (e:Exception)
            {
                Log.i("TAG", "getDataFromApi: "+e.message)
            }
        }
    }
    fun insertDataToDataBase(weatherDataBase: Model,context: Context)
    {
        val database= CurrentWeatherDatabas.getInstance(context)
        GlobalScope.launch {
            Dispatchers.IO
            database.weatherDao().insertCurrentWeather(weatherDataBase)
        }
    }
    fun getAllDataFromDB(context: Context): LiveData<List<Model>>
    {
        val dataBase= CurrentWeatherDatabas.getInstance(context)
        return dataBase.weatherDao().getAllWeather()
    }
    fun deleteWeather(weatherDataBase: Model,context: Context)
    {
        GlobalScope.launch {
            Dispatchers.IO
            val dataBase= CurrentWeatherDatabas.getInstance(context).weatherDao().deleteWeather(weatherDataBase)
        }
    }



    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }



}