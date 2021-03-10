
package com.app.weatherapp.mvvm.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.weatherapp.R
import com.app.weatherapp.mvvm.ui.AlarmFragment.CHANNEL_ID


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
    }

    override fun onReceive(context: Context, intent: Intent) {
        val bData = intent.extras
        if (bData!!["title"] == "activity_app") {
            Log.e(TAG, "Alarm rings!!")


            val notifyBuilder = getNotificationBuilder(context, intent)

            val closeIntent = Intent(context, NotificationReceiver::class.java)
            val closePendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID, closeIntent, PendingIntent.FLAG_ONE_SHOT
            )
            notifyBuilder.addAction(
                R.drawable.ic_baseline_close_24,
                context.getString(R.string.close),
                closePendingIntent
            )

            (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                NOTIFICATION_ID,
                notifyBuilder.build()
            )
        }
    }

    private fun getNotificationBuilder(
        context: Context,
        intent: Intent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentTitle("Alarm Ring")
            setContentText(intent.getStringExtra("time"))
            setSmallIcon(R.drawable.ic_baseline_access_time_24)
            setAutoCancel(false)
            priority = NotificationCompat.PRIORITY_MAX
            setOngoing(true)
        }
    }
}
