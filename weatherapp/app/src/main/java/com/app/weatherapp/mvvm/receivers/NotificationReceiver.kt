
package com.app.weatherapp.mvvm.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.weatherapp.mvvm.receivers.AlarmReceiver.Companion.NOTIFICATION_ID


class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_CLOSE_NOTIFICATION =
            "com.example.weatherapp.myalarm.ACTION_CLOSE_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {

        // Cancel the notification.
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
    }
}
