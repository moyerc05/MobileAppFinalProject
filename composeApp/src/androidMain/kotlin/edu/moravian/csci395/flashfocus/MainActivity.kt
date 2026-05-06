package edu.moravian.csci395.flashfocus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.moravian.csci395.flashfocus.data.getRoomDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            App(getRoomDatabase(getDatabaseBuilder(this)))
        }
    }
    private fun createNotificationChannel() {
        // Notification Channels are only required (and available) on Android 8.0+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Study Reminders"
            val descriptionText = "Notifies you when your cooldown timer ends"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            // Notice this ID exactly matches the one you used in TimerScreen.kt!
            val channel = NotificationChannel("studyReminderChannelId", name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
