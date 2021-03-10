package com.app.weatherapp.mvvm.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.databinding.WeatherFragmentBinding
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.model.Settings
import com.app.weatherapp.mvvm.data.remote.*
import com.app.weatherapp.mvvm.data.remote.DataTransformation.getImage
import com.app.weatherapp.mvvm.ui.WeatherFragmentData.DailyAdapter
import com.app.weatherapp.mvvm.ui.WeatherFragmentData.HoursAdapter
import com.bumptech.glide.Glide
import com.notificationman.library.NotificationMan


import im.delight.android.location.SimpleLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeatherFragment : Fragment() {

    private lateinit var binding: WeatherFragmentBinding
    private lateinit var hoursAdapter: HoursAdapter
    private  lateinit var dailyDays: DailyAdapter
    private lateinit var viewModel: WeatherViewModel
    var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null
    var arraylistWeather: List<Model> = ArrayList()
    private lateinit var arrayListWeather:ArrayList<Model>
   lateinit var sp: SharedPreferences
    private lateinit var weatherDataBase:Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false)
        sp= PreferenceManager.getDefaultSharedPreferences(context)

        arrayListWeather= ArrayList()
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        hoursAdapter = HoursAdapter(requireContext())
        dailyDays = DailyAdapter(requireContext())
        if(InternetAvailable(requireContext()) ) {
            loadSettings()



            if (sp.getBoolean("USE_DEVICE_LOCATION", true)) {
                location = SimpleLocation(context)
                if (!location!!.hasLocationEnabled()) {
                    SimpleLocation.openSettings(context)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            requireActivity()!!,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity()!!,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            const.REQUEST_CODE
                        )
                    } else {
                        location = SimpleLocation(context)
                        Settings.latitude = String.format("%.6f", location?.latitude)
                        Settings.longitude = String.format("%.6f", location?.longitude)
                        Log.e("LAT1", "" + latitude)
                        Log.e("LONG1", "" + longitude)

                    }
                }
                viewModel.getDataFromOpenWeatherApi(Settings.latitude, Settings.longitude)
            } else {
                viewModel.getDataFromOpenWeatherApi(Settings.mapLat2, Settings.mapLong2)

                //viewModel.getPlaceFromOpenWeatherApi(city = Settings.customLocations)
                /*viewModel.getPlaceFromOpenWeather().observe(viewLifecycleOwner, Observer {
                })*/
            }

            /* viewModel.getData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
             arraylistWeather = listOf(it)
             initData()
         })*/

            viewModel.getDataFromOpenWeather().observe(viewLifecycleOwner, Observer {
                GlobalScope.launch {
                    try {

                        if(sp.getBoolean("USE_NOTIFICATIONS_ALERT", true)) {

                            if(it.alerts!=null)
                            {
                                var lllist = listOf(3L, 900L,(it.alerts.get(0).start).toLong(),(it.alerts.get(0).end).toLong())
                                for(i in lllist.indices) {
                                    NotificationMan
                                        .Builder(
                                            requireContext().applicationContext,
                                            "the activity's path that you want to open when the notification is clicked"
                                        )
                                        .setTitle(Settings.mapcity2) // optional
                                        .setDescription(
                                            when {
                                                sp.getString("UNIT_SYSTEM", "") == "STANDARD" -> {
                                                    "Alert!"+it.alerts.get(0).sender_name+"\n"+it.alerts[0].event+"\n"+it.alerts.get(0).description + " "+ + it.current.temp+"°K"
                                                }
                                                sp.getString("UNIT_SYSTEM", "") == "METRIC" -> {
                                                    "Alert!"+it.alerts.get(0).sender_name+"\n"+it.alerts[0].event+"\n"+it.alerts.get(0).description + " "+ + it.current.temp+"°C"

                                                }
                                                sp.getString("UNIT_SYSTEM", "") == "IMPERIAL" -> {
                                                    "Alert!"+it.alerts.get(0).sender_name+"\n"+it.alerts[0].event+"\n"+it.alerts.get(0).description + " "+ + it.current.temp+"°F"

                                                }


                                                else -> {
                                                    "Alert!"+it.alerts.get(0).sender_name+"\n"+it.alerts[0].event+"\n"+it.alerts.get(0).description + " "+ + it.current.temp

                                                }
                                            }


                                        ) // optional
                                        .setThumbnailImageUrl("https://icon-library.com/images/alerts-icon/alerts-icon-15.jpg") // optional
                                        .setTimeInterval(lllist[i]) // needs secs - default is 5 secs
                                        .setNotificationType(NotificationMan.NOTIFICATION_TYPE_IMAGE) // optional - default type is TEXT
                                        .fire()
                                }
                            }

                            var llist = listOf(1L, 900L,3600L)
                            Log.i("timetime", "getDatatime: " + llist.size)
                            for (x in llist.indices) {
                                NotificationMan
                                    .Builder(
                                        requireContext().applicationContext,
                                        "com.app.weatherapp.mvvm.ui.WeatherFragment.WeatherFragment"
                                    ) // make sure class path match with your project architecture
                                    .setTitle(it.timezone) // optional
                                    .setDescription(
                                        when {
                                            sp.getString("UNIT_SYSTEM", "") == "STANDARD" -> {
                                                it.current.weather.get(0).description + " " + it.current.temp + "°K"
                                            }
                                            sp.getString("UNIT_SYSTEM", "") == "METRIC" -> {
                                                it.current.weather.get(0).description + " " + it.current.temp + "°C"

                                            }
                                            sp.getString("UNIT_SYSTEM", "") == "IMPERIAL" -> {
                                                it.current.weather.get(0).description + " " + it.current.temp + "°F"

                                            }


                                            else -> {
                                                it.current.weather.get(0).description + " " + it.current.temp

                                            }
                                        }
                                    ) // optional
                                    .setThumbnailImageUrl(
                                        "http://openweathermap.org/img/wn/${
                                            (it).current.weather.get(
                                                0
                                            ).icon
                                        }.png"
                                    ) // optional
                                    .setTimeInterval(llist.get(x)) // needs secs - default is 5 secs
                                    .setNotificationType(NotificationMan.NOTIFICATION_TYPE_IMAGE) // optional - default type is TEXT
                                    .fire()
                            }
                        }
                        Dispatchers.IO
                        weatherDataBase = setDataonDB(it)
                        val db =
                            CurrentWeatherDatabas.getInstance(requireContext().applicationContext)
                                .weatherDao().insertCurrentWeather(setDataonDB(it))
                        viewModel.insertDataToDataBase(
                            weatherDataBase,
                            requireContext().applicationContext
                        )
                        withContext(Dispatchers.Main)
                        {
                            if (getData() == null) {
                                Toast.makeText(context, "DB is null", Toast.LENGTH_SHORT).show()
                            } else {
                                getData()

                            }
                        }
                    } catch (e: Exception) {
                        Log.i("eeee", "onCreateView: " + e.message)
                    }
                }
                arraylistWeather = listOf(it)
                // binding.locationGPS = locationGps

            })

        }
        else
        {
            getData()
        }
        //ivWeather = v.image
        return binding.getRoot()
    }
    fun InternetAvailable(context: Context): Boolean {
        var wifiConnected = false
        var mobileConnected = false
        var isConnected = false
        val service = Context.CONNECTIVITY_SERVICE
        val connectivityManager = context.getSystemService(service) as ConnectivityManager?
        val networkInfo = connectivityManager?.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            wifiConnected = networkInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = networkInfo.type == ConnectivityManager.TYPE_MOBILE
            if (wifiConnected || mobileConnected) {
                isConnected = true
            }
        } else {
            isConnected = false
        }
        return isConnected
    }

    private fun getData()
    {
         viewModel.getAllDataFromDB(requireContext().applicationContext).observe(viewLifecycleOwner,
             Observer {
                 if (it == null) {
                     Toast.makeText(context, "There is no data in app", Toast.LENGTH_SHORT).show()
                 } else {

                     //arrayListWeather.add(it.get(0))
                     if(sp.getString("UNIT_SYSTEM", "") == "STANDARD")
                     {
                         binding.tempreture.text = it.get(0).current.temp.toString()+"°K"
                         binding.wind.text = it.get(0).current.wind_speed.toString()+" m/s"
                     }
                     else if( sp.getString("UNIT_SYSTEM", "") == "METRIC")
                     {
                         binding.tempreture.text = it.get(0).current.temp.toString()+"°C"
                         binding.wind.text = it.get(0).current.wind_speed.toString()+" m/s"

                     }
                     else if(sp.getString("UNIT_SYSTEM", "") == "IMPERIAL")
                     {
                         binding.tempreture.text = it.get(0).current.temp.toString()+"°F"
                         binding.wind.text = it.get(0).current.wind_speed.toString()+" M/s"
                     }
                     else
                     {
                         binding.tempreture.text = it.get(0).current.temp.toString()
                         binding.wind.text = it.get(0).current.wind_speed.toString()
                     }
                     binding.updatedAt.text = DataTransformation.dateNow
                     binding.address.text = it.get(0).timezone
                     binding.pressure.text = it.get(0).current.pressure.toString()
                     binding.humidty.text = it.get(0).current.humidity.toString()
                     binding.status.text = it.get(0).current.weather[0].description
                     binding.sunrise.text = SimpleDateFormat(
                         "hh:mm a",
                         Locale.ENGLISH
                     ).format(Date((it.get(0).current.sunrise).toLong() * 1000))
                     binding.sunset.text = SimpleDateFormat(
                         "hh:mm a",
                         Locale.ENGLISH
                     ).format(Date((it.get(0).current.sunset).toLong() * 1000))

                     Glide.with(binding.imgStatus).load(getImage(it.get(0).current.weather[0].icon))
                         .into(binding.imgStatus)
                     hoursAdapter = HoursAdapter(requireContext())
                     dailyDays = DailyAdapter(requireContext())
                     var list: List<Hourly> = it.get(0).hourly
                     hoursAdapter.setData(list)
                     binding.rycContainer.adapter = hoursAdapter

                     var list2: List<Daily> = it.get(0).daily
                     dailyDays.setData(list2)
                     binding.rycContainer2.adapter = dailyDays
                     binding.rycContainer.layoutManager =
                         LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                     binding.rycContainer2.layoutManager =
                         LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                     hoursAdapter = HoursAdapter(requireContext())
                     dailyDays = DailyAdapter(requireContext())

                 }

             })
    }
    private fun setDataonDB(model: Model): Model {
        val list = arrayListOf<Model>()
        for (listItem in listOf(model)) {
            list.add(model)
        }

        val weatherModelDB = Model(
            0,
            model.lat,
            model.lon,
            model.timezone,
            model.timezone_offset,
            model.current,
            model.hourly,
            model.daily,
            model.alerts
        )

        return weatherModelDB
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == const.REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                Settings.latitude = String.format("%.6f", location?.latitude)
                Settings.longitude = String.format("%.6f", location?.longitude)
                Log.e("LAT", "" + latitude)
                Log.e("LONG", "" + longitude)

                viewModel.getDataFromOpenWeatherApi(Settings.latitude, Settings.longitude)

            } else {
                Toast.makeText(context, " rrr:P", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    private fun loadSettings() {
        sp= PreferenceManager.getDefaultSharedPreferences(context)
        val unit_system= sp.getString("UNIT_SYSTEM", "")
        val language_system=sp.getString("LANGUAGE_SYSTEM", "")
        val deviceLocation=sp.getBoolean("USE_DEVICE_LOCATION", false)
        val notifications=sp.getBoolean("USE_NOTIFICATIONS_ALERT", false)
        //val customLocations=sp.getString("CUSTOM_LOCATION", "")
        val mapLocation=sp.getBoolean("MAP_LOCATION",false)
        if (unit_system != null) {
            Settings.unitSystem=unit_system
        }
        if(language_system!=null)
        {
            Settings.languageSystem=language_system
        }
        if (deviceLocation!=null)
        {
            Settings.deviceLocation=deviceLocation
        }
        if(notifications!=null)
        {
            Settings.notifications=notifications
        }
        if(mapLocation!=null)
        {
            Settings.mapLocations=mapLocation
        }
    }



    companion object {
        private const val TAG = "WeatheraFragment"

        @JvmStatic
        fun newInstance() =
            WeatherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}