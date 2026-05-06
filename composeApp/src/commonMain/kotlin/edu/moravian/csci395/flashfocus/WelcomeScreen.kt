@file:Suppress("ktlint:standard:no-wildcard-imports", "ktlint:standard:function-naming")

package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import edu.moravian.csci395.flashfocus.data.ALL_BLOBS
import edu.moravian.csci395.flashfocus.data.BlobEntity
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import studyblobs.composeapp.generated.resources.AppIcon
import studyblobs.composeapp.generated.resources.Res
import studyblobs.composeapp.generated.resources.my_collection
import studyblobs.composeapp.generated.resources.start_studying
import studyblobs.composeapp.generated.resources.statistics
import studyblobs.composeapp.generated.resources.welcome_title

@Serializable
object WelcomeScreen

/**
 * Main landing screen of the app.
 * Displays:
 * - Floating collectible blobs
 * - App title and theming
 * - Navigation buttons to core features
 * @param viewModel Provides blob collection state.
 * @param onStart Navigates to timer setup.
 * @param onViewStats Navigates to statistics screen.
 * @param onViewCollection Navigates to collection screen.
 */
@Composable
fun WelcomeScreen(
    viewModel: AppViewModel,
    onStart: () -> Unit,
    onViewStats: () -> Unit,
    onViewCollection: () -> Unit,
) {
    val blobs by viewModel.blobs.collectAsState(initial = emptyList())
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .onSizeChanged { containerSize = it },
    ) {
        FloatingBlobsLayer(
            blobIds = blobs.map { it.blobId },
            containerSize = containerSize,
        )

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
        ) {
            val isLandscape = maxWidth > maxHeight

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                ) {
                    WelcomeTitleSection(isLandscape = isLandscape)

                    Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 24.dp))

                    Button(
                        onClick = onStart,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                    ) {
                        Text(stringResource(Res.string.start_studying))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onViewCollection,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                    ) {
                        Text(stringResource(Res.string.my_collection))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onViewStats,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                    ) {
                        Text(stringResource(Res.string.statistics))
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeTitleSection(isLandscape: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (!isLandscape) {
            Image(
                painter = painterResource(Res.drawable.AppIcon),
                contentDescription = "Flash Focus Logo",
                modifier = Modifier.size(120.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(
            text = stringResource(Res.string.welcome_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Renders and animates a layer of floating blobs.
 * Blobs move continuously and bounce within the screen bounds.
 * @param blobIds IDs of blobs to render.
 * @param containerSize Size of the screen/container for boundary calculations.
 */
@Composable
private fun FloatingBlobsLayer(
    blobIds: List<String>,
    containerSize: IntSize,
) {
    val blobs =
        remember(blobIds) {
            blobIds.map { createFloatingBlob(it) }
        }

    LaunchedEffect(containerSize, blobs) {
        while (true) {
            blobs.forEach { it.update(containerSize) }
            delay(16)
        }
    }

    Box(Modifier.fillMaxSize()) {
        blobs.forEach { blob ->
            FloatingBlob(blob)
        }
    }
}

/**
 * Represents a single floating blob with position and velocity.
 * Handles movement and boundary collision.
 * @property blobId Identifier used to map to blob visuals.
 */
private class FloatingBlob(
    val blobId: String,
) {
    var x by mutableStateOf(0f)
    var y by mutableStateOf(0f)

    var dx by mutableStateOf(randomVelocity())
    var dy by mutableStateOf(randomVelocity())

    companion object {
        private fun randomVelocity(): Float {
            val speed = (1..4).random().toFloat()
            return if (listOf(true, false).random()) speed else -speed
        }
    }

    fun update(container: IntSize) {
        x += dx
        y += dy

        val size = 80f

        if (x < 0 || x > container.width - size) {
            dx *= -1
        }

        if (y < 0 || y > container.height - size) {
            dy *= -1
        }
    }
}

/**
 * Creates a floating blob with randomized starting position.
 */
private fun createFloatingBlob(blobId: String): FloatingBlob =
    FloatingBlob(blobId).apply {
        x = (0..300).random().toFloat()
        y = (0..600).random().toFloat()
    }

/**
 * Displays a single floating blob image at its current position.
 */
@Composable
private fun FloatingBlob(blob: FloatingBlob) {
    val blobInfo = ALL_BLOBS.find { it.id == blob.blobId } ?: return

    Image(
        painter = painterResource(blobInfo.image),
        contentDescription = null,
        modifier =
            Modifier
                .offset {
                    IntOffset(blob.x.toInt(), blob.y.toInt())
                }.size(80.dp)
                .alpha(0.8f),
    )
}
