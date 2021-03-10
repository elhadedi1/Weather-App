package com.app.weatherapp.mvvm.ui.FavoriteFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.app.weatherapp.mvvm.data.remote.Model
import com.app.weatherapp.mvvm.data.remote.PlaceReponseOneApi.PlaceResponseOneModel
import com.app.weatherapp.mvvm.data.remote.RetrofitClient
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel() : ViewModel() {
    private val apiClient = RetrofitClient
    private val disposable = CompositeDisposable()

   // private var weatherDao= WeatherDataBase.getInstance(application)
    private var getPlaceMutableLiveData: MutableLiveData<PlaceResponseOneModel> =
        MutableLiveData<PlaceResponseOneModel>()
    private var getDataMutableLiveData:MutableLiveData<Model> =MutableLiveData<Model>()
    fun getDataFromOpenWeather():MutableLiveData<Model>
    {
        return getDataMutableLiveData
    }
    fun getDataFromApi(latitude: String, longitude: String,lang:String,units:String)
    {
        GlobalScope.launch {
            Dispatchers.IO
            try{

                val response= apiClient.getDataFromGps(latitude, longitude, lang,
                    units).execute()


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
                        Log.i("TAGTAGTTTTT", "getPlaceFromApi: "+myResponseAPI.get(0).name)
                    }
                }
            }catch (e:Exception)
            {
                Log.i("TAGTAG", "getPlaceFromApi: "+e.message)
            }
        }
    }

    fun insertDataToDataBase(weatherDataBase: Favourite,context: Context)
    {
        val database=CurrentWeatherDatabas.getInstance(context)
        GlobalScope.launch {
            Dispatchers.IO
            database.weatherDao().insertWeatherFavourite(weatherDataBase)
        }
    }
    fun getAllDataFromDB(context: Context): LiveData<List<Favourite>>
    {
        val dataBase=CurrentWeatherDatabas.getInstance(context)
        return dataBase.weatherDao().getAllFavouirteData()
    }
    fun deleteWeather(weatherDataBase: Favourite,context: Context)
    {
        GlobalScope.launch {
            Dispatchers.IO
            val dataBase=CurrentWeatherDatabas.getInstance(context).weatherDao().deleteWeatherFavourite(weatherDataBase)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}