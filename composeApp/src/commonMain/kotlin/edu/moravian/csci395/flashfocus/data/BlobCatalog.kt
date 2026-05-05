package edu.moravian.csci395.flashfocus.data
import flashfocus.composeapp.generated.resources.Res
import flashfocus.composeapp.generated.resources.blob_blue
import flashfocus.composeapp.generated.resources.blob_gold
import flashfocus.composeapp.generated.resources.blob_green
import flashfocus.composeapp.generated.resources.blob_orange
import flashfocus.composeapp.generated.resources.blob_pink
import flashfocus.composeapp.generated.resources.blob_red
import flashfocus.composeapp.generated.resources.blob_twins
import org.jetbrains.compose.resources.DrawableResource

data class BlobInfo(
    val id: String,
    val displayName: String,
    val image: DrawableResource,
    val rarityLabel: String,
    val spawnChance: String
)

val ALL_BLOBS = listOf(
    BlobInfo("pink_common", "Pink Blob", Res.drawable.blob_pink, "Common", "30%"),
    BlobInfo("green_common", "Green Blob", Res.drawable.blob_green, "Common", "30%"),
    BlobInfo("orange_common", "Orange Blob", Res.drawable.blob_orange, "Common", "20%"),
    BlobInfo("blue_rare", "Blue Blob", Res.drawable.blob_blue, "Rare", "10%"),
    BlobInfo("red_rare", "Red Blob", Res.drawable.blob_red, "Rare", "10%"),
    BlobInfo("twins_rare", "Twins Blob", Res.drawable.blob_twins, "Rare", "10%"),
    BlobInfo("gold_epic", "Gold Blob", Res.drawable.blob_gold, "Epic", "5%"),
)