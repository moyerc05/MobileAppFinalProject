package edu.moravian.csci395.flashfocus

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
import com.patrykandpatrick.vico.compose.cartesian.*
import com.patrykandpatrick.vico.compose.cartesian.axis.*
import com.patrykandpatrick.vico.compose.cartesian.data.*
import com.patrykandpatrick.vico.compose.cartesian.layer.*
import edu.moravian.csci395.flashfocus.data.MilestoneEntity
import edu.moravian.csci395.flashfocus.data.StudySessionEntity
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import studyblobs.composeapp.generated.resources.*
import kotlin.time.Instant

@Serializable
object StatsScreen

/**
 * Displays study statistics and history.
 * Shows:
 * Weekly study time chart,
 * Milestone progress tracking,
 * Session history list,
 * and option to reset all data.
 * @param viewModel Provides session and milestone data.
 * @param onReset Clears all stored data.
 */
@Composable
fun StatsScreen(
    viewModel: AppViewModel,
    onReset: () -> Unit,
) {
    val sessions by viewModel.sessions.collectAsState(initial = emptyList())
    val milestones by viewModel.milestones.collectAsState(initial = emptyList())

    val totalMinutes by viewModel.totalStudyTime.collectAsState(initial = 0)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Text(
                text = stringResource(Res.string.statistics_title),
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChartSection(sessions)

            Spacer(modifier = Modifier.height(24.dp))

            MilestonesSection(
                milestones = milestones,
                totalMinutes = totalMinutes,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.study_history_title),
                style = MaterialTheme.typography.titleLarge,
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
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(Res.string.reset_all_data_button))
            }

            Spacer(modifier = Modifier.height(32.dp)) // extra bottom padding for scroll comfort
        }
    }
}

/**
 * Displays a bar chart of study time grouped by day of week.
 * @param sessions List of study sessions.
 */
@Composable
private fun ChartSection(sessions: List<StudySessionEntity>) {
    Text(
        text = stringResource(Res.string.study_time_chart_title),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (sessions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(stringResource(Res.string.no_study_data_placeholder))
        }
        return
    }

    // Group sessions by day of week
    val sessionsByDay = sessions.groupBy { session ->
        getDayOfWeekShort(session.startTime)
    }

    val orderedDays = stringArrayResource(Res.array.days_of_week_short)
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
                },
            ),
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    )
}

/**
 * Converts epoch time to a short day-of-week string.
 * @param epochMillis Timestamp in milliseconds.
 * @return Short day string.
 */
@Composable
fun getDayOfWeekShort(epochMillis: Long): String {
    val dateTime = Instant
        .fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val monday = stringResource(Res.string.mon_day)
    val tuesday = stringResource(Res.string.tues_day)
    val wednesday = stringResource(Res.string.wednes_day)
    val thursday = stringResource(Res.string.thurs_day)
    val friday = stringResource(Res.string.fri_day)
    val saturday = stringResource(Res.string.satur_day)
    val sunday = stringResource(Res.string.sun_day)

    return when (dateTime.dayOfWeek) {
        DayOfWeek.MONDAY -> monday
        DayOfWeek.TUESDAY -> tuesday
        DayOfWeek.WEDNESDAY -> wednesday
        DayOfWeek.THURSDAY -> thursday
        DayOfWeek.FRIDAY -> friday
        DayOfWeek.SATURDAY -> saturday
        DayOfWeek.SUNDAY -> sunday
    }
}

/**
 * Displays milestone progress bars and completion indicators.
 * @param milestones Achieved milestones.
 * @param totalMinutes Total accumulated study time.
 */
@Composable
private fun MilestonesSection(
    milestones: List<MilestoneEntity>,
    totalMinutes: Int,
) {
    val milestoneTargets = listOf(30, 60, 90, 120, 150, 180, 210, 240, 270, 300)

    val achievedValues = milestones.map { it.value }

    val nextTarget = milestoneTargets.firstOrNull { it !in achievedValues }

    Text(
        text = stringResource(Res.string.milestones_title),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (nextTarget != null) {
        val progress = (totalMinutes.toFloat() / nextTarget).coerceAtMost(1f)

        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(text = "$nextTarget min")

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    } else {
        Text(stringResource(Res.string.milestones_completed))
    }
}

/**
 * Displays a single study session entry.
 * @param session Session data.
 */
@Composable
private fun SessionItem(session: StudySessionEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = formatEpochMillis(session.startTime),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(Res.string.session_duration, session.durationMinutes),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
