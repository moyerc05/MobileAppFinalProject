package edu.moravian.csci395.flashfocus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "TOTAL_TIME", "STREAK", etc.
    val value: Int,   // e.g. 100 minutes, 7 days
    val achievedAt: Long
)