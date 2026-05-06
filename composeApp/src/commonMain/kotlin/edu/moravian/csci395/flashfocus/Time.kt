package edu.moravian.csci395.flashfocus

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Returns the current time in milliseconds since the Unix epoch. Can be converted to a
 * human-readable format using the [formatEpochMillis] function.
 *
 * Requires platform-specific implementations.
 */
expect fun currentTimeMillis(): Long

/**
 * Converts the given epoch milliseconds to a human-readable date and time string in the format
 * "YYYY-MM-DD HH:MM". The date and time are displayed in the user's local time zone.
 */
fun formatEpochMillis(epochMillis: Long): String {
    val dateTime =
        Instant
            .fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    return "${dateTime.date} $hour:$minute"
}
