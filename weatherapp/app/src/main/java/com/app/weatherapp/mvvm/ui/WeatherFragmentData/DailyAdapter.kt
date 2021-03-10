package com.app.weatherapp.mvvm.ui.WeatherFragmentData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.mvvm.data.remote.Daily
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyAdapter(val context:Context) : RecyclerView.Adapter<DailyAdapter.WeatherViewHolder>() {
    private var weatherList : List<Daily> =ArrayList()

    class WeatherViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imgView: ImageView = itemview.imgWeather
        var tvName: TextView = itemview.weatherDate
        var tvDate: TextView = itemview.weatherDescription
        var weatherTemp: TextView =itemview.weatherTemperature
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.tvName.text = weatherList[position].weather[0].description
        holder.tvDate.text = SimpleDateFormat("EEE, dd/MM/yyyy", Locale.ENGLISH).format(Date(((weatherList[position].dt).toLong()*1000)))
       var sp= PreferenceManager.getDefaultSharedPreferences(context)
        if(sp.getString("UNIT_SYSTEM", "")=="STANDARD") {
            holder.weatherTemp.text = weatherList[position].temp.day.toString()+"°K"
        }
        else if(sp.getString("UNIT_SYSTEM", "")=="METRIC")
        {
            holder.weatherTemp.text = weatherList[position].temp.day.toString()+"°C"

        }
        else
        {
            holder.weatherTemp.text = weatherList[position].temp.day.toString()+"°F"

        }
        Glide.with(holder.itemView).load("http://openweathermap.org/img/wn/${(weatherList)[position].weather[0].icon}.png").into(holder.imgView)


    }
    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun setData(weatherList: List<Daily>) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }


}