package com.abdallah.sarrawi.myapplication



import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        // عرض الإشعار باستخدام NotificationUtils
        NotificationUtils.showNotification(
            applicationContext,
            "عنوان الإشعار",
            "محتوى الإشعار هنا"
        )
        Result.success()
    }
}
