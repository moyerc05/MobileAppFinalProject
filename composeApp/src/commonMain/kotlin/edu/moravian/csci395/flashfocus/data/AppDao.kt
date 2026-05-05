package edu.moravian.csci395.flashfocus.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // Study Sessions
    @Insert
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT * FROM StudySessionEntity ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT SUM(durationMinutes) FROM StudySessionEntity")
    suspend fun getTotalStudyTime(): Int?

    // Milestones
    @Insert
    suspend fun insertMilestone(milestone: MilestoneEntity)

    @Query("SELECT * FROM MilestoneEntity")
    fun getMilestones(): Flow<List<MilestoneEntity>>

    @Query("SELECT * FROM MilestoneEntity")
    suspend fun getMilestonesOnce(): List<MilestoneEntity>

    // Blobs
    @Insert
    suspend fun insertBlob(blob: BlobEntity)

    @Query("SELECT * FROM BlobEntity")
    fun getAllBlobs(): Flow<List<BlobEntity>>

    @Query("SELECT COUNT(*) FROM BlobEntity")
    fun getBlobCount(): Flow<Int>

    // Reset (for stats screen button)
    @Query("DELETE FROM StudySessionEntity")
    suspend fun clearSessions()

    @Query("DELETE FROM MilestoneEntity")
    suspend fun clearMilestones()

    @Query("DELETE FROM BlobEntity")
    suspend fun clearBlobs()
}