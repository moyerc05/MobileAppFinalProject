package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import edu.moravian.csci395.flashfocus.data.ALL_BLOBS
import edu.moravian.csci395.flashfocus.data.BlobInfo
import flashfocus.composeapp.generated.resources.Res
import flashfocus.composeapp.generated.resources.blob_placeholder
import org.jetbrains.compose.resources.painterResource

@Serializable
object CollectionScreen

@Composable
fun CollectionScreen(
    viewModel: AppViewModel
) {
    val blobs by viewModel.blobs.collectAsState(initial = emptyList())

    // Extract unlocked blob IDs
    val unlockedIds = blobs.map { it.blobId }.toSet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Collection",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ALL_BLOBS) { blob ->
                BlobItem(
                    blob = blob,
                    isUnlocked = unlockedIds.contains(blob.id)
                )
            }
        }
    }
}

@Composable
private fun BlobItem(
    blob: BlobInfo,
    isUnlocked: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {

            Image(
                painter = painterResource(
                    if (isUnlocked) blob.image
                    else Res.drawable.blob_placeholder
                ),
                contentDescription = blob.displayName,
                modifier = Modifier
                    .size(80.dp)
                    .alpha(if (isUnlocked) 1f else 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isUnlocked) blob.displayName else "???",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = blob.spawnChance,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
