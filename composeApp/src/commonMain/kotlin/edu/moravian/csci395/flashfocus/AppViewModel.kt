package edu.moravian.csci395.flashfocus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.moravian.csci395.flashfocus.data.AppDao
import edu.moravian.csci395.flashfocus.data.BlobEntity
import edu.moravian.csci395.flashfocus.data.MilestoneEntity
import edu.moravian.csci395.flashfocus.data.StudySessionEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EndSessionState(
    val durationMinutes: Int = 0,
    val unlockedBlobIds: List<String> = emptyList(),
)

class AppViewModel(
    private val dao: AppDao,
) : ViewModel() {
    // Timer State
    private val _timerDuration = MutableStateFlow(0)
    val timerDuration: StateFlow<Int> = _timerDuration

    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var timerJob: Job? = null

    private var startTimeMillis: Long = 0

    private val _endSessionState = MutableStateFlow(EndSessionState())
    val endSessionState: StateFlow<EndSessionState> = _endSessionState

    // Public Timer Controls
    fun setTimer(minutes: Int) {
        val seconds = minutes * 60
        _timerDuration.value = seconds
        _timeRemaining.value = seconds
    }

    fun startTimer() {
        if (_isRunning.value) return

        _isRunning.value = true
        startTimeMillis = currentTimeMillis()

        timerJob = viewModelScope.launch {
            while (_timeRemaining.value > 0 && _isRunning.value) {
                delay(1000)
                _timeRemaining.value -= 1
            }

            if (_timeRemaining.value <= 0) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timeRemaining.value = _timerDuration.value
    }

    // Timer Completion Logic
    private fun onTimerFinished() {
        _isRunning.value = false
        timerJob?.cancel()

        val endTime = currentTimeMillis()
        val durationMinutes = _timerDuration.value / 60

        viewModelScope.launch {
            dao.insertSession(
                StudySessionEntity(
                    startTime = startTimeMillis,
                    endTime = endTime,
                    durationMinutes = durationMinutes,
                ),
            )

            val unlockedBlobs = checkMilestonesAndReturnBlobs()

            // store data for EndScreen
            _endSessionState.value = EndSessionState(
                durationMinutes = durationMinutes,
                unlockedBlobIds = unlockedBlobs,
            )
        }
    }

    // Milestones Logic
    private suspend fun checkMilestonesAndReturnBlobs(): List<String> {
        val totalMinutes = dao.getTotalStudyTime() ?: 0
        val existingMilestones = dao.getMilestonesOnce()

        val milestoneTargets = listOf(60, 300, 600)

        val unlockedBlobs = mutableListOf<String>()

        for (target in milestoneTargets) {
            val alreadyAchieved = existingMilestones.any { it.value == target }

            if (totalMinutes >= target && !alreadyAchieved) {
                // Save milestone
                dao.insertMilestone(
                    MilestoneEntity(
                        type = "TOTAL_TIME",
                        value = target,
                        achievedAt = currentTimeMillis(),
                    ),
                )

                // Unlock blob
                val blobId = generateRandomBlobId()

                dao.insertBlob(
                    BlobEntity(
                        blobId = blobId,
                        unlockedAt = currentTimeMillis(),
                        isNew = true,
                    ),
                )

                unlockedBlobs.add(blobId)
            }
        }

        return unlockedBlobs
    }

    // Blob Unlock Logic
    private suspend fun unlockRandomBlob() {
        val blobId = generateRandomBlobId()

        dao.insertBlob(
            BlobEntity(
                blobId = blobId,
                unlockedAt = currentTimeMillis(),
                isNew = true,
            ),
        )
    }

    private fun generateRandomBlobId(): String {
        val weightedBlobs = listOf(
            "pink_common" to 30,
            "green_common" to 30,
            "orange_common" to 20,
            "blue_rare" to 10,
            "red_rare" to 10,
            "twins_rare" to 10,
            "gold_epic" to 5,
        )

        val totalWeight = weightedBlobs.sumOf { it.second }

        val randomValue = (1..totalWeight).random()

        var cumulative = 0

        for ((blobId, weight) in weightedBlobs) {
            cumulative += weight
            if (randomValue <= cumulative) {
                return blobId
            }
        }

        return weightedBlobs.last().first
    }

    // Data for UI
    val sessions: Flow<List<StudySessionEntity>> =
        dao.getAllSessions()

    val blobs: Flow<List<BlobEntity>> =
        dao.getAllBlobs()

    val milestones: Flow<List<MilestoneEntity>> =
        dao.getMilestones()

    val totalStudyTime: Flow<Int> =
        sessions.map { list -> list.sumOf { it.durationMinutes } }

    // Reset Data
    fun resetAllData() {
        viewModelScope.launch {
            dao.clearSessions()
            dao.clearMilestones()
            dao.clearBlobs()
        }
    }
}
