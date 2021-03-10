package com.app.weatherapp.mvvm.ui.AlarmFragment

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherapp.R
import com.app.weatherapp.data.local.alarmData.Alarm
import com.app.weatherapp.databinding.AlarmFragmentBinding
import com.app.weatherapp.mvvm.data.model.Settings
import com.app.weatherapp.mvvm.receivers.AlarmReceiver
import java.util.*


const val CHANNEL_ID = "Alarm Channel"

class AlarmFragment : Fragment() {
    private lateinit var binding : AlarmFragmentBinding
    private lateinit var alarmViewModel: AlarmViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.alarm_fragment, container, false)


        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)


        binding.btnAddAlarm.setOnClickListener {
            Navigation.findNavController(it!!).navigate(R.id.action_alarmFragment_to_editAlarmFragment)


        }
        if((!Settings.hours.isEmpty())&&(!Settings.minutes.isEmpty())) {

            val hour = Settings.hours
            val minute = Settings.minutes

            val alarm =
                Alarm(hour = hour.toInt(), minute = minute.toInt())
            alarmViewModel.insert(alarm)
            addAlarm(alarm)


        }
        initRecycleView()




        createNotificationChannel()
        return binding.getRoot()

    }

    private fun initRecycleView(){
        val adapter = AdapterAlarm(requireContext())
        binding.AlarmRecycler?.adapter = adapter
        binding.AlarmRecycler?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        alarmViewModel.allAlarms.observe(requireActivity(), Observer { alarms ->
            alarms?.let { adapter.setAlarms(alarms) }
        })

    }

    private fun addAlarm(alarm: Alarm) {
        val cal: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
        }



        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.addCategory("ID.${cal[Calendar.HOUR_OF_DAY]}.${cal[Calendar.MINUTE]}.${cal[Calendar.SECOND]}")
        intent.putExtra("title", "activity_app")
        intent.putExtra(
            "time",
            "Alarmtime ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}:${cal[Calendar.SECOND]}"
        )

        val alarmIntent =
            PendingIntent.getBroadcast(requireContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_description)
                enableVibration(true)
                enableLights(false)
            }
            val notificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        fun newInstance() =
            AlarmFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}