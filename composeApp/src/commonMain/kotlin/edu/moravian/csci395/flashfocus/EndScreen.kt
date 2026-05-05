package edu.moravian.csci395.flashfocus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.moravian.csci395.flashfocus.data.ALL_BLOBS
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
object EndScreen

@Composable
fun EndScreen(
    viewModel: AppViewModel,
    onDone: () -> Unit
) {
    val endState by viewModel.endSessionState.collectAsState()

    val totalMinutes by viewModel.totalStudyTime.collectAsState(initial = 0)

    val milestoneTargets = listOf(60, 300, 600)

    val nextMilestone = milestoneTargets.firstOrNull { totalMinutes < it }

    val progress = if (nextMilestone != null) {
        (totalMinutes.toFloat() / nextMilestone).coerceAtMost(1f)
    } else {
        1f
    }

    val unlockedBlob = ALL_BLOBS.find { it.id == endState.unlockedBlobId }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Session Complete!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You studied for ${endState.durationMinutes} minutes",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Blob Unlock Section
        if (unlockedBlob != null) {

            Text(
                text = "You unlocked a blob!",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(unlockedBlob.image),
                contentDescription = unlockedBlob.displayName,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(unlockedBlob.displayName)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Milestone Progress
        if (nextMilestone != null) {

            Text(
                text = "Progress to next milestone:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("$totalMinutes / $nextMilestone minutes")

        } else {
            Text("All milestones completed!")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}