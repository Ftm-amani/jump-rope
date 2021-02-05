package com.example.myapplication.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R


fun NotificationManager.setNotification(title:String , message:String,context : Context){
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.rope)
    val bigIconStyle = NotificationCompat.BigPictureStyle().bigPicture(largeIcon)

    val builder = NotificationCompat.Builder(context, context.getString(R.string.channel_jump_finish))
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setLargeIcon(largeIcon)
        .setStyle(bigIconStyle)
        .setSmallIcon(R.drawable.ic_skipping_rope)
        .setContentIntent(pendingIntent)


    notify(101, builder.build())
}
