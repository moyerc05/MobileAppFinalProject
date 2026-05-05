package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.datetime.*

// Vico imports
import com.patrykandpatrick.vico.compose.cartesian.*
import com.patrykandpatrick.vico.compose.cartesian.axis.*
import com.patrykandpatrick.vico.compose.cartesian.data.*
import com.patrykandpatrick.vico.compose.cartesian.layer.*
import edu.moravian.csci395.flashfocus.data.MilestoneEntity
import edu.moravian.csci395.flashfocus.data.StudySessionEntity
import kotlinx.coroutines.launch

@Serializable
object StatsScreen

@Composable
fun StatsScreen(
    viewModel: AppViewModel,
    onReset: () -> Unit
) {
    val sessions by viewModel.sessions.collectAsState(initial = emptyList())
    val milestones by viewModel.milestones.collectAsState(initial = emptyList())

    val totalMinutes by viewModel.totalStudyTime.collectAsState(initial = 0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChartSection(sessions)

            Spacer(modifier = Modifier.height(24.dp))

            MilestonesSection(
                milestones = milestones,
                totalMinutes = totalMinutes
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Study History",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(sessions) { session ->
            SessionItem(session)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset All Data")
            }

            Spacer(modifier = Modifier.height(32.dp)) // extra bottom padding for scroll comfort
        }
    }
}

@Composable
private fun ChartSection(sessions: List<StudySessionEntity>) {

    Text(
        text = "Study Time Chart",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (sessions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No study data yet.")
        }
        return
    }

    // Group sessions by day of week
    val sessionsByDay = sessions.groupBy { session ->
        getDayOfWeekShort(session.startTime)
    }

    val orderedDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val minutesPerDay = orderedDays.map { day ->
        sessionsByDay[day]?.sumOf { it.durationMinutes } ?: 0
    }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(minutesPerDay) {
        modelProducer.runTransaction {
            columnSeries {
                series(minutesPerDay)
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = { _, x, _ ->
                    orderedDays.getOrNull(x.toInt()) ?: ""
                }
            ),
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

fun getDayOfWeekShort(epochMillis: Long): String {
    val dateTime = kotlin.time.Instant
        .fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return when (dateTime.dayOfWeek) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

@Composable
private fun MilestonesSection(
    milestones: List<MilestoneEntity>,
    totalMinutes: Int
) {
    val milestoneTargets = listOf(60, 300, 600)

    Text(
        text = "Milestones",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    milestoneTargets.forEach { target ->
        val achieved = milestones.any { it.value == target }

        val progress = (totalMinutes.toFloat() / target).coerceAtMost(1f)

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "${target} min ${if (achieved) "✅" else ""}"
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SessionItem(session: StudySessionEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = formatEpochMillis(session.startTime),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Duration: ${session.durationMinutes} min",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
