package edu.moravian.csci395.flashfocus

//noinspection SuspiciousImport
import android.R
import android.app.NotificationManager
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration =
    AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.drawable.ic_dialog_info,
        notificationIconColor =
            androidx.compose.ui.graphics
                .Color(red = 100, blue = 100, green = 100),
        notificationChannels =
            listOf(
                AlarmeeNotificationChannel(
                    id = "studyReminderChannelId",
                    name = "Study Reminders",
                    importance = NotificationManager.IMPORTANCE_DEFAULT,
                ),
            ),
    )
