package com.app.weatherapp.mvvm.ui.FavoriteFragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.mvvm.data.local.CurrentWeatherDatabas
import com.app.weatherapp.mvvm.data.remote.Favourite
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.favorite_items.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FavoirteAdapter(val context:Context,val cellClickListener: CellClickListener): RecyclerView.Adapter<FavoirteAdapter.WeatherViewHolder>() {
    var weatherList:List<Favourite> =ArrayList()

    class WeatherViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var imgView: ImageView = itemview.imgFWeather
        var tvName: TextView = itemview.txtViewPlace
        var tvDate: TextView = itemview.TextViewDescription
        var weatherTemp: TextView =itemview.txtViewTemp
        var textPlace:TextView=itemview.txtViewCity
        var imgViewDelete:ImageView=itemview.imgViewDelete2

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoirteAdapter.WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_items, parent, false)
        return FavoirteAdapter.WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoirteAdapter.WeatherViewHolder, position: Int) {

        holder.tvName.text = weatherList[position].current.weather[0].description
        holder.tvDate.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(((weatherList[position].current.dt).toLong()*1000)))
        holder.weatherTemp.text = weatherList[position].current.temp.toString()


        Glide.with(holder.itemView).load("http://openweathermap.org/img/wn/${(weatherList)[position].current.weather.get(0).icon}.png").into(holder.imgView)
        holder.textPlace.text = weatherList[position].cityName
        if(weatherList.size==0)
        {
            Toast.makeText(context,"No Data either to be deleted",Toast.LENGTH_LONG).show()
            return
        }
        else {
            holder.imgViewDelete.setOnClickListener(

                    View.OnClickListener {
                        GlobalScope.launch {
                            try {
                                Dispatchers.IO


                                val db = CurrentWeatherDatabas.getInstance(context = context.applicationContext)
                                        .weatherDao().deleteWeatherFavourite(weatherList.get(position))


                            } catch (e: Exception) {
                                Log.i("DELETE", "onBindViewHolder: a" + e.message)
                            }


                        }

                    }
            )
        }

        var favourite:Favourite=weatherList[position]
        holder.itemView.setOnClickListener {
                 cellClickListener.onCellClickListener(favourite)
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun setData(weatherList: List<Favourite>) {

        this.weatherList=weatherList
        notifyDataSetChanged()
    }

}