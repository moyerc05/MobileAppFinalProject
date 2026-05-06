package edu.moravian.csci395.flashfocus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BlobEntity(
    @PrimaryKey
    val blobId: String,
    val unlockedAt: Long,
    val isNew: Boolean = true,
)
