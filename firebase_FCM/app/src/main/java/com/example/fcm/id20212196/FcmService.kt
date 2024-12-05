package com.example.fcm.id20212196

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM TOKEN",token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM RECV","${message.notification?.title}-${message.notification?.body}")

        sendNotification(message.notification?.title,message.notification?.body,message.data)
    }

    private fun sendNotification(title:String?,body:String?, data:Map<String,String>){

        val intent = Intent(this,MainActivity::class.java)

        try{
            intent.putExtra("page",data.getValue("page"))
        }catch (e:Exception){
            Log.e("FCM",e.message.toString())
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.img_notification_icon)
            .setContentTitle(title ?: "제목")
            .setContentText(body ?: "내용")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0,notificationBuilder.build())
    }


}