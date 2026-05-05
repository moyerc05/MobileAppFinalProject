package edu.moravian.csci395.flashfocus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val durationMinutes: Int
)