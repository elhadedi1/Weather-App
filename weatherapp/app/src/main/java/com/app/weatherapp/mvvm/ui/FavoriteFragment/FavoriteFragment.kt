package com.app.weatherapp.mvvm.ui.FavoriteFragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.databinding.FavoriteFragmentBinding
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.model.Settings
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.app.weatherapp.mvvm.data.remote.Model
import com.app.weatherapp.mvvm.data.remote.PlaceDataOpenApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(),CellClickListener {
    lateinit var binding:FavoriteFragmentBinding
    private lateinit var currentAdapter:FavoirteAdapter
    private var place:List<PlaceDataOpenApiService> =ArrayList()
    private lateinit var arrayListWeather:ArrayList<Favourite>
    private lateinit var viewModel:FavoriteViewModel
    private lateinit var weatherDataBase:Favourite

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        arrayListWeather= ArrayList()

        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_fragment, container, false)

        btnAdd()
        var sp= PreferenceManager.getDefaultSharedPreferences(context)
        val unit_system= sp.getString("UNIT_SYSTEM", "").toString()
        val language_system=sp.getString("LANGUAGE_SYSTEM", "").toString()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        viewModel.getDataFromApi(Settings.mapLat, Settings.mapLong, language_system, unit_system)




        if(InternetAvailable(requireContext()) ){
            viewModel.getDataFromOpenWeather().observe(viewLifecycleOwner, Observer {
                GlobalScope.launch {
                    try {
                        Dispatchers.IO
                        weatherDataBase = setDataonDB(it)
                        val db =
                            CurrentWeatherDatabas.getInstance(requireContext().applicationContext)
                                .weatherDao().insertWeatherFavourite(
                                setDataonDB(
                                    it
                                )
                            )

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
                        Log.i("message", "onCreateView: " + e.message)
                    }
                }


            })
        }else{
            getData()
        }


        Log.i("Ahmed", "onon: " + Settings.fLatitude)


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
        viewModel.getAllDataFromDB(requireContext().applicationContext).observe(
            viewLifecycleOwner,
            Observer {
                Log.i("RREVO", "EVOEN: " + it)
                if (it == null) {
                    Toast.makeText(context, "There is no data in app", Toast.LENGTH_SHORT).show()
                } else {
                    //arrayListWeather.add(it.get(0))
                    currentAdapter = FavoirteAdapter(requireContext().applicationContext, this)
                    binding.recyclerContainer.adapter = currentAdapter
                    binding.recyclerContainer.layoutManager =
                        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    currentAdapter.setData(it)
                    currentAdapter.notifyDataSetChanged()
                }
            })

    }


private fun btnAdd()
{
    binding.btnAdd.setOnClickListener { view?.let{Navigation.findNavController(it!!).navigate(R.id.action_favoriteFragment_to_mapsFragment)} }
}

    private fun setDataonDB(model: Model): Favourite {
        val list = arrayListOf<Model>()
        for (listItem in listOf(model)) {
            list.add(model)
        }

        val weatherModelDB = Favourite(
            model.id,
            model.lat,
            model.lon,
            model.timezone,
            model.timezone_offset,
            model.current,
            model.hourly,
            model.daily,
            Settings.mapCity,
            Settings.cityPlace
        )

        return weatherModelDB
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onCellClickListener(favourite: Favourite) {
        arguments = Bundle().apply {
            putParcelable("favourite_details", favourite)
        }
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_favoriteFragment_to_homeFragment, arguments)
        }
    }



}
interface CellClickListener {
    fun onCellClickListener(favourite: Favourite)
}