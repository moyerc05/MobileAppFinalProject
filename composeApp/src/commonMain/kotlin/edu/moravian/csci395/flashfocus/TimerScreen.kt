package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tweener.alarmee.model.Alarmee
import com.tweener.alarmee.model.AndroidNotificationConfiguration
import com.tweener.alarmee.model.AndroidNotificationPriority
import com.tweener.alarmee.model.IosNotificationConfiguration
import com.tweener.alarmee.model.RepeatInterval
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import studyblobs.composeapp.generated.resources.*

// --- Alarmee & DateTime Imports ---
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import com.tweener.alarmee.rememberAlarmeeService
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Serializable
object TimerScreen

@Composable
fun TimerScreen(
    viewModel: AppViewModel,
    onTimerFinished: () -> Unit,
) {
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val totalDuration by viewModel.timerDuration.collectAsState()

    // 1. Initialize the Alarmee Service
    val alarmService = rememberAlarmeeService(
        platformConfiguration = createAlarmeePlatformConfiguration()
    )
    val localService = alarmService.local

    // Navigate and schedule notification when timer hits zero
    LaunchedEffect(timeRemaining) {
        if (timeRemaining == 0 && totalDuration > 0) {

            // Calculate exactly 24 hours from the moment the timer finishes
            val tomorrowTime = kotlin.time.Clock.System.now().plus(24.hours) //Change as necessary for presentation
            val scheduledTime = tomorrowTime.toLocalDateTime(TimeZone.currentSystemDefault())

            // 2. Schedule the local notification
            localService.schedule(
                alarmee = Alarmee(
                    uuid = "dailyStudyReminder", // Keeps the ID constant so new sessions reset the 24h clock
                    notificationTitle = "📚 Time to focus!",
                    notificationBody = "It's been 24 hours since your last study session. Keep your streak going!",
                    scheduledDateTime = scheduledTime,
                    //repeatInterval = RepeatInterval.Daily, // Will repeat every day until they study again
                    androidNotificationConfiguration = AndroidNotificationConfiguration(
                        priority = AndroidNotificationPriority.DEFAULT,
                        channelId = "studyReminderChannelId", // Matches our Android Config
                    ),
                    iosNotificationConfiguration = IosNotificationConfiguration(),
                )
            )

            onTimerFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.study_timer_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        // Timer Display
        Text(
            text = formatTime(timeRemaining),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        // Progress Indicator
        if (totalDuration > 0) {
            val progress = if (totalDuration == 0) {
                0f
            } else {
                1f - (timeRemaining.toFloat() / totalDuration.toFloat())
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
            )
        }

        // Controls
        Button(
            onClick = {
                if (isRunning) {
                    viewModel.pauseTimer()
                } else {
                    viewModel.startTimer()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (isRunning) stringResource(Res.string.pause_button) else stringResource(Res.string.start_button))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.resetTimer()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.reset_timer_button))
        }
    }
}

/**
 * Converts seconds → MM:SS format
 */
private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
