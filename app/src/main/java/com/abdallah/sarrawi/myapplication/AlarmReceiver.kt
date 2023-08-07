package com.abdallah.sarrawi.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.abdallah.sarrawi.myapplication.NotificationUtils

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // قم بإضافة الكود الخاص بإرسال الإشعار هنا
        NotificationUtils.showNotification(
            context,
            "عنوان الإشعار",
            "محتوى الإشعار هنا"
        )
    }
}
