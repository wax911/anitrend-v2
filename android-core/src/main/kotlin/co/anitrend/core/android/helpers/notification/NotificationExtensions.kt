package co.anitrend.core.android.helpers.notification

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.helpers.notification.NotificationHelper.Companion.POST_NOTIFICATION_PERMISSION_REQUEST_CODE
import co.anitrend.core.android.helpers.notification.config.NotificationConfig


fun Context.hasNotificationPermissionFor(config: NotificationConfig): Boolean {
    val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED
    } else {
        NotificationManagerCompat.from(this).areNotificationsEnabled()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasPermission) {
        val channel = NotificationManagerCompat.from(this).getNotificationChannel(config.name)
        if (channel != null && channel.importance == config.importance) {
            return false
        }
    }
    return hasPermission
}

fun FragmentActivity.requestPostNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            /* context = */ this,
            /* permission = */ Manifest.permission.POST_NOTIFICATIONS,
        ) != PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            /* activity = */ this,
            /* permissions = */ arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            /* requestCode = */ POST_NOTIFICATION_PERMISSION_REQUEST_CODE,
        )
    }
}
