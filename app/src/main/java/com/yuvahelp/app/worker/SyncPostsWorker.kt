package com.yuvahelp.app.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yuvahelp.app.data.PostRepository

class SyncPostsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repository = PostRepository.getInstance(applicationContext)
        val count = repository.syncPosts()
        if (count > 0) {
            showNotification("Yuva Help", "New updates available: $count posts")
        }
        return Result.success()
    }

    private fun showNotification(title: String, content: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "Yuva Help Updates", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }

    companion object {
        const val WORK_NAME = "sync_posts_worker"
        private const val CHANNEL_ID = "yuva_help_updates"
    }
}
