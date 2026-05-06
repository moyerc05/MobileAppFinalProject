package edu.moravian.csci395.flashfocus

import studyblobs.composeapp.generated.resources.AppIcon
import android.app.NotificationManager
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import edu.moravian.csci395.flashfocus.R
actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration {
    return AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.drawable.app_icon,
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