package edu.moravian.csci395.flashfocus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import org.jetbrains.compose.resources.stringResource
import studyblobs.composeapp.generated.resources.Res
import studyblobs.composeapp.generated.resources.back_to_home
import studyblobs.composeapp.generated.resources.blob
import studyblobs.composeapp.generated.resources.milestones_completed
import studyblobs.composeapp.generated.resources.minutes
import studyblobs.composeapp.generated.resources.progress_to_milestone
import studyblobs.composeapp.generated.resources.session_complete
import studyblobs.composeapp.generated.resources.you_studied_for
import studyblobs.composeapp.generated.resources.you_unlocked

@Serializable
object EndScreen

@Suppress("ktlint:standard:function-naming", "ktlint:standard:kdoc")
/**
 * Displayed after a study session completes.
 * Shows:
 * Session duration,
 * Newly unlocked blobs,
 * and progress toward next milestone.
 * @param viewModel Provides session results and progress data.
 * @param onDone Navigates back to the home screen.
 */
@Composable
fun EndScreen(
    viewModel: AppViewModel,
    onDone: () -> Unit,
) {
    val endState by viewModel.endSessionState.collectAsState()

    val totalMinutes by viewModel.totalStudyTime.collectAsState(initial = 0)

    val milestoneTargets = listOf(30, 60, 90, 120, 150, 180, 210, 240, 270, 300)

    val nextMilestone = milestoneTargets.firstOrNull { totalMinutes < it }

    val progress =
        if (nextMilestone != null) {
            (totalMinutes.toFloat() / nextMilestone).coerceAtMost(1f)
        } else {
            1f
        }

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "milestone_progress",
    )

    val unlockedBlobs = endState.unlockedBlobIds

    val unlockedBlobInfos =
        unlockedBlobs.mapNotNull { id ->
            ALL_BLOBS.find { it.id == id }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.session_complete),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.you_studied_for) + "${endState.durationMinutes} " + stringResource(Res.string.minutes),
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Blob Unlock Section
        if (unlockedBlobInfos.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.you_unlocked) + "${unlockedBlobInfos.size} " + stringResource(Res.string.blob),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                unlockedBlobInfos.forEach { blob ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Image(
                            painter = painterResource(blob.image),
                            contentDescription = blob.displayName,
                            modifier = Modifier.size(80.dp),
                        )

                        Text(
                            text = blob.displayName,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Milestone Progress
        if (nextMilestone != null) {
            Text(
                text = stringResource(Res.string.progress_to_milestone),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(12.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("$totalMinutes / $nextMilestone " + stringResource(Res.string.minutes))
        } else {
            Text(stringResource(Res.string.milestones_completed))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(Res.string.back_to_home))
        }
    }
}
