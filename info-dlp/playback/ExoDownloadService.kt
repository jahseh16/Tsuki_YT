package com.zionhuang.music.playback

import android.app.Notification
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExoDownloadService : DownloadService(
    1, // notificationId
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    "download_channel_id",
    com.zionhuang.music.R.string.app_name,
    0
) {
    @Inject
    lateinit var downloadManagerInstance: DownloadManager

    override fun getDownloadManager(): DownloadManager {
        return downloadManagerInstance
    }

    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val channelId = "download_channel_id"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = android.app.NotificationChannel(
                    channelId,
                    "Downloads",
                    android.app.NotificationManager.IMPORTANCE_LOW
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
        return androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setContentTitle("Downloading")
            .setSmallIcon(com.zionhuang.music.R.drawable.download)
            .build()
    }
}
