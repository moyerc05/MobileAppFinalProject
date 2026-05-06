package edu.moravian.csci395.flashfocus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val value: Int,
    val achievedAt: Long,
)
