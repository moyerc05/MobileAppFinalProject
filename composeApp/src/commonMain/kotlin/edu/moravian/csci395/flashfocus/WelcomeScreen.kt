package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import flashfocus.composeapp.generated.resources.Res
import flashfocus.composeapp.generated.resources.AppIcon

@Serializable
object WelcomeScreen

@Composable
fun WelcomeScreen(
    viewModel: AppViewModel,
    onStart: () -> Unit,
    onViewStats: () -> Unit,
    onViewCollection: () -> Unit,
) {
    // We use the background color from your theme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Add the Logo Image
            // Ensure you put the image in commonMain/composeResources/drawable/
            Image(
                painter = painterResource(Res.drawable.AppIcon),
                contentDescription = "Flash Focus Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Text
            Text(
                text = "Welcome to\nFlash Focus",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Master your study sessions",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // 3. Primary Action Button
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Makes the button a bit taller and easier to tap
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Start Study Session",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Secondary Action Buttons
            OutlinedButton(
                onClick = onViewCollection,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("My Collection")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onViewStats,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("View Stats")
            }
        }
    }
}