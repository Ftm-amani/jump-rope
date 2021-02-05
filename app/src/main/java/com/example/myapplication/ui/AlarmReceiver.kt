package com.example.myapplication.ui

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.myapplication.util.setNotification

class AlarmReceiver : BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context, intent: Intent?) {
//        Toast.makeText(context, "Time finished", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "onReceive: called")
        val notificationManager = ContextCompat
            .getSystemService(context, NotificationManager::class.java)

        notificationManager?.setNotification("Jumping finished!","finish",context)
    }

}