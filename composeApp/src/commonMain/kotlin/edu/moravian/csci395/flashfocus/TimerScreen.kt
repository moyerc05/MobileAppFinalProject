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
import kotlinx.serialization.Serializable

@Serializable
object TimerScreen

@Composable
fun TimerScreen(
    viewModel: AppViewModel,
    onTimerFinished: () -> Unit
) {

    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val totalDuration by viewModel.timerDuration.collectAsState()

    // Navigate when timer hits zero
    LaunchedEffect(timeRemaining) {
        if (timeRemaining == 0 && totalDuration > 0) {
            onTimerFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Study Timer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Timer Display
        Text(
            text = formatTime(timeRemaining),
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Progress Indicator
        if (totalDuration > 0) {
            val progress = if (totalDuration == 0) 0f
            else 1f - (timeRemaining.toFloat() / totalDuration.toFloat())

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRunning) "Pause" else "Start")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.resetTimer()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
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