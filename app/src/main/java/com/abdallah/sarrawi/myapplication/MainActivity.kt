package com.abdallah.sarrawi.myapplication

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Switch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var switch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private val NOTIFICATION_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // إنشاء قناة الإشعار إذا لم تكن موجودة بالفعل
        NotificationUtils.createNotificationChannel(this)

        // الحصول على مرجع للـ Switch من ملف الواجهة
        switch = findViewById(R.id.notification_switch)

        // تهيئة الـ SharedPreferences
        sharedPreferences = getSharedPreferences("notification_pref", MODE_PRIVATE)

        // طلب إذن الإشعارات
        requestNotificationPermission()

        // قراءة حالة التبديل من SharedPreferences وتحديث حالة الـ Switch
        switch.isChecked = sharedPreferences.getBoolean("notification_enabled", false)

        // استمع لتغيير حالة الـ Switch
        switch.setOnCheckedChangeListener { _, isChecked ->
            // حفظ حالة التبديل في SharedPreferences
            sharedPreferences.edit().putBoolean("notification_enabled", isChecked).apply()

            if (isChecked) {
                // إذا تم تفعيل الـ Switch، قم بجدولة إرسال الإشعار يوميًا في الساعة 10 صباحًا
                scheduleNotification()
            } else {
                // إذا تم إيقاف الـ Switch، قم بإلغاء جدولة إرسال الإشعار
                cancelNotification()
            }
        }
    }

    //... (الدوال الأخرى)




private fun scheduleNotification() {
        // الحصول على مرجع لمدير المنبهات
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // إنشاء Intent لاستدعاء BroadcastReceiver عند وقت الإشعار
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        // تحديد وقت الإشعار اليومي عند الساعة 10 صباحًا
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (System.currentTimeMillis() >= timeInMillis) {
                // إذا كانت الساعة قد مرت بالفعل، قم بجدولة الإشعار في اليوم التالي
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // جدولة استدعاء BroadcastReceiver عند الساعة 10 صباحًا يوميًا
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

        Log.d("MainActivity", "تم جدولة الإشعار عند الساعة 10 صباحًا يوميًا.")
    }

    private fun cancelNotification() {
        // الحصول على مرجع لمدير المنبهات
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // إنشاء Intent لاستدعاء BroadcastReceiver عند وقت الإشعار
        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        // إلغاء جدولة استدعاء BroadcastReceiver
        alarmManager.cancel(alarmIntent)

        Log.d("MainActivity", "تم إلغاء جدولة الإشعار.")
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // لم يتم منح إذن الإشعارات، نقوم بطلبه
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.VIBRATE),
                NOTIFICATION_PERMISSION_CODE
            )
        } else {
            // تم منح إذن الإشعارات مسبقًا، نقوم بتفعيل الإشعارات
            sharedPreferences.edit().putBoolean("notification_enabled", true).apply()
            scheduleNotification()
        }
        Log.d("MainActivity", "تم طلب إذن الإشعارات.")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح إذن الإشعارات، نقوم بتفعيل الإشعارات
                sharedPreferences.edit().putBoolean("notification_enabled", true).apply()
                scheduleNotification()
            } else {
                // تم رفض إذن الإشعارات، نقوم بإلغاء جدولة الإشعار
                switch.isChecked = false
                sharedPreferences.edit().putBoolean("notification_enabled", false).apply()
                cancelNotification()
            }
        }
        Log.d("MainActivity", "تم معالجة نتيجة طلب إذن الإشعارات.")
    }

    /*
    switch.setOnCheckedChangeListener { _, isChecked ->
            //...

            if (isChecked) {
                //...

                // إنشاء PendingIntent باستخدام NavGraph للمنبه الأول في الساعة 10 صباحًا
                scheduleNotification(10, 0, R.id.targetFragment)

                // إنشاء PendingIntent باستخدام NavGraph للمنبه الثاني في الساعة 8 مساءً
                scheduleNotification(20, 0, R.id.anotherFragment)
            } else {
                //...
            }
        }
    }

    private fun scheduleNotification(hour: Int, minute: Int, destinationId: Int) {
        // الحصول على مرجع لمدير المنبهات
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // إنشاء Intent لاستدعاء BroadcastReceiver عند وقت الإشعار
        val intent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.nav_graph) // تحديد NavGraph الخاص بالتطبيق
            .setDestination(destinationId) // تحديد الوجهة المستهدفة
            .createTaskStackBuilder().intents[0]

        val alarmIntent = PendingIntent.getActivity(this, 0, intent, 0)

        // تحديد وقت الإشعار اليومي
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (System.currentTimeMillis() >= timeInMillis) {
                // إذا كان الوقت قد مر بالفعل، قم بجدولة الإشعار في اليوم التالي
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        // جدولة استدعاء BroadcastReceiver حسب الوقت المحدد
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

        Log.d("MainActivity", "تم جدولة الإشعار عند الساعة $hour:$minute يوميًا.")
    }

     */
}
