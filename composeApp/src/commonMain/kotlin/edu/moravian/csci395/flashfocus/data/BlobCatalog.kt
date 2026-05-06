package edu.moravian.csci395.flashfocus.data
import org.jetbrains.compose.resources.DrawableResource
import studyblobs.composeapp.generated.resources.Res
import studyblobs.composeapp.generated.resources.blob_blue
import studyblobs.composeapp.generated.resources.blob_gold
import studyblobs.composeapp.generated.resources.blob_green
import studyblobs.composeapp.generated.resources.blob_orange
import studyblobs.composeapp.generated.resources.blob_pink
import studyblobs.composeapp.generated.resources.blob_red
import studyblobs.composeapp.generated.resources.blob_twins

data class BlobInfo(
    val id: String,
    val displayName: String,
    val image: DrawableResource,
    val rarityLabel: String,
    val spawnChance: String,
)

val ALL_BLOBS =
    listOf(
        BlobInfo("pink_common", "Pink Blob", Res.drawable.blob_pink, "Common", "30%"),
        BlobInfo("green_common", "Green Blob", Res.drawable.blob_green, "Common", "30%"),
        BlobInfo("orange_common", "Orange Blob", Res.drawable.blob_orange, "Common", "20%"),
        BlobInfo("blue_rare", "Blue Blob", Res.drawable.blob_blue, "Rare", "10%"),
        BlobInfo("red_rare", "Red Blob", Res.drawable.blob_red, "Rare", "10%"),
        BlobInfo("twins_rare", "Twins Blob", Res.drawable.blob_twins, "Rare", "10%"),
        BlobInfo("gold_epic", "Gold Blob", Res.drawable.blob_gold, "Epic", "5%"),
    )
