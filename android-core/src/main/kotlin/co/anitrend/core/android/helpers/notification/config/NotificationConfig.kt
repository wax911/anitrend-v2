package co.anitrend.core.android.helpers.notification.config

import android.app.NotificationManager

enum class NotificationConfig(
    val title: String,
    val description: String,
    val importance: Int,
    val group: String,
) {
    GENERAL(
        title = "General",
        description = "AniTrend specific notifications",
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        group = "co.anitrend.notification.group.GENERAL",
    ),
    ANILIST(
        title = "AniList",
        description = "AniList related notifications",
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        group = "co.anitrend.notification.group.ANILIST",
    ),
    ANNOUNCEMENT(
        title = "Announcements",
        description = "Announcements and other important information",
        importance = NotificationManager.IMPORTANCE_HIGH,
        group = "co.anitrend.notification.group.ANNOUNCEMENT",
    )
}
