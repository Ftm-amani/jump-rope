package com.example.myapplication.util

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessagingServ"
    

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToBackend(token)
        Log.i(TAG, "onNewToken: ")
    }

    private fun sendTokenToBackend(token: String) {
        //send token to server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAG, "onMessageReceived: ${message.notification?.title}")
        sendNotification(message.notification?.title ?: "-",message.notification?.body ?: "-")

    }

    private fun sendNotification(title: String, message: String) {

        val notificationManager =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)

        notificationManager?.setNotification(title, message, applicationContext)
    }

}