package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable


@Serializable
object WelcomeScreen

@Composable
fun WelcomeScreen(
    viewModel: AppViewModel,
    onStart: () -> Unit,
    onViewStats: () -> Unit,
    onViewCollection: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Uses backgroundLight/Dark
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Study App",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary // Uses primaryLight/Dark
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Study Session")
            }

            Button(
                onClick = onViewStats,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Stats")
            }

            Button(
                onClick = onViewCollection,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Collection")
            }
        }
    }
}
