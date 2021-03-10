package com.app.weatherapp.mvvm.ui.FavoriteFragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.app.weatherapp.R
import com.app.weatherapp.databinding.HomeFragmentBinding
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.remote.DataTransformation
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

   private lateinit var favourite: Favourite
    private lateinit var binding:HomeFragmentBinding
    var flag = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favourite = it.getParcelable("favourite_details")!!
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        var sp= PreferenceManager.getDefaultSharedPreferences(context)

        binding.humidty2.text=favourite.current.humidity.toString()
        binding.tempreture2.text = favourite.current.temp.toString()
        binding.wind2.text = favourite.current.wind_speed.toString()

        binding.pressure2.text=favourite.current.pressure.toString()
        binding.sunset2.text= SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(Date((favourite.current.sunset).toLong() * 1000))
        binding.sunrise2.text=SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(Date((favourite.current.sunrise).toLong() * 1000))
        binding.address2.text=favourite.cityName
        Glide.with(binding.imgStatus2).load(DataTransformation.getImage(favourite.current.weather[0].icon))
            .into(binding.imgStatus2)
        binding.status2.text=favourite.current.weather.get(0).description
        binding.updatedAt2.text=DataTransformation.dateNow
        if (flag) {
            GlobalScope.launch {
                val db = CurrentWeatherDatabas.getInstance(requireContext()).weatherDao()
                    .insertWeatherFavourite(favourite)

            }
        } else {
            Log.i("DetailsFragment", "Movie already Exist")
        }

        return binding.getRoot()
    }


    companion object {
        fun newInstance() = HomeFragment().apply {
             arguments=Bundle().apply {

             }
        }

    }

}