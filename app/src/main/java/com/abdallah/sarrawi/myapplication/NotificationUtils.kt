package com.abdallah.sarrawi.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationUtils {

    private const val NOTIFICATION_ID = 1
    private const val CHANNEL_ID = "my_new_notification_channel"
    private const val CHANNEL_NAME = "My New Notification Channel"

    fun showNotification(context: Context, title: String, content: String) {
        createNotificationChannel(context)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // تفعيل الاهتزاز عند عرض الإشعار
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND) // تشغيل صوت وأضواء الإشعار

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
//package com.abdallah.sarrawi.myapplication
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import androidx.navigation.NavDeepLinkBuilder
//
//object NotificationUtils {
//
//    private const val NOTIFICATION_ID = 1
//    private const val CHANNEL_ID = "my_new_notification_channel"
//    private const val CHANNEL_NAME = "My New Notification Channel"
//
//    fun showNotification(context: Context, title: String, content: String) {
//        createNotificationChannel(context)
//
//        // تحديد الوجهة المستهدفة عند النقر على الإشعار باستخدام NavComponent
//        val pendingIntent = NavDeepLinkBuilder(context)
//            .setGraph(R.navigation.nav_graph) // تحديد NavGraph الخاص بالتطبيق
//            .setDestination(R.id.targetFragment) // تحديد الوجهة المستهدفة
//            .createPendingIntent()
//
//        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle(title)
//            .setContentText(content)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent) // ربط الوجهة المستهدفة مع الإشعار
//            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000)) // تفعيل الاهتزاز عند عرض الإشعار
//            .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND) // تشغيل صوت وأضواء الإشعار
//
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }
//
//    fun createNotificationChannel(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//}
