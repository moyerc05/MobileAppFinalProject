package edu.moravian.csci395.flashfocus

import android.R
import android.app.NotificationManager
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration {
    return AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.drawable.ic_dialog_info, // Replace with your app's notification icon resource (e.g., R.drawable.ic_notification)
        notificationIconColor = androidx.compose.ui.graphics.Color(red = 100, blue =100 , green =100),
        notificationChannels = listOf(
            AlarmeeNotificationChannel(
                id = "studyReminderChannelId",
                name = "Study Reminders",
                importance = NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    )
}