package co.anitrend.core.android.helpers.notification

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.anitrend.core.android.helpers.notification.config.NotificationConfig

class NotificationHelper(
    private val notificationManager: NotificationManagerCompat
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannels() {
        val channels = NotificationConfig.entries.map { config ->
            return with (NotificationChannel(config.name, config.title, config.importance)) {
                description = config.description
                group = config.group
                setShowBadge(true)
                enableLights(false)
            }
        }
        notificationManager.createNotificationChannels(channels)
    }

    companion object {
        const val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 0x12

        fun notificationVisibilityFor(isAdult: Boolean) =
            if (isAdult) NotificationCompat.VISIBILITY_SECRET else NotificationCompat.VISIBILITY_PUBLIC
    }
}
