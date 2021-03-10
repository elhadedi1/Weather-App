package com.app.weatherapp.mvvm.ui.AlarmFragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.data.local.alarmData.Alarm
import com.app.weatherapp.mvvm.receivers.AlarmReceiver
import java.util.*


class AdapterAlarm(
    private val context: Context
) : RecyclerView.Adapter<AdapterAlarm.WordViewHolder>() {

    companion object {
        private const val TAG = "AdapterAlarm"
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var alarms = emptyList<Alarm>()

    private val alarmViewModel =
        ViewModelProvider(context as ViewModelStoreOwner).get(AlarmViewModel::class.java)

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmItemView: TextView = itemView.findViewById(R.id.txtViewDate2)
        val delete: ImageView = itemView.findViewById(R.id.imgViewDelete2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.alarm_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = alarms[position]

        holder.alarmItemView.text = "${current.hour}:${current.minute}"
        holder.delete.setOnClickListener {
            alarmViewModel.delete(current)
            cancelAlarm(current)
        }

    }

    internal fun setAlarms(alarms: List<Alarm>) {
        this.alarms = alarms
        notifyDataSetChanged()
    }

    override fun getItemCount() = alarms.size

    @SuppressLint("LogNotTimber")
    private fun cancelAlarm(alarm: Alarm) {
        val cal: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
        }

        Log.e(
            TAG,
            "alarm cancel time: ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}:${cal[Calendar.SECOND]}"
        )
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addCategory("ID.${cal[Calendar.HOUR_OF_DAY]}.${cal[Calendar.MINUTE]}.${cal[Calendar.SECOND]}")
        intent.putExtra("title", "activity_app")
        intent.putExtra(
            "time",
            "Alarmtime ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}:${cal[Calendar.SECOND]}"
        )
        val pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }

}