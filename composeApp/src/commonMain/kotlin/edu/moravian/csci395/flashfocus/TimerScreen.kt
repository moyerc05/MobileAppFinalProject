package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Study Timer",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Placeholder for your actual timer logic later
        Text(
            text = "25:00",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(onClick = { /* TODO: Add timer start logic here */ }) {
            Text("Start Timer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // This button triggers the navigation action passed in from App.kt
        Button(onClick = onNavigateBack) {
            Text("Back to Welcome")
        }
    }
}