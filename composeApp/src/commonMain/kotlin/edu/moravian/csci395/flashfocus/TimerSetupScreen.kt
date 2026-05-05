package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import flashfocus.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@Serializable
object TimerSetupScreen

@Composable
fun TimerSetupScreen(
    viewModel: AppViewModel,
    onStartTimer: () -> Unit,
) {
    val invalidMinutes = stringResource(Res.string.invalid_minutes_error)
    val twentyFive = stringResource(Res.string.twenty_five_minute_text)
    val fortyFive = stringResource(Res.string.forty_five_minute_text)
    val hourText = stringResource(Res.string.hour_text)

    var minutesInput by rememberSaveable { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = stringResource(Res.string.set_timer_text),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = minutesInput,
                onValueChange = {
                    minutesInput = it
                    errorText = null
                },
                label = { Text(stringResource(Res.string.minutes_placeholder)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            errorText?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetButton(25) { minutesInput = twentyFive }
                PresetButton(45) { minutesInput = fortyFive }
                PresetButton(60) { minutesInput = hourText }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val minutes = minutesInput.toIntOrNull()

                    if (minutes == null || minutes <= 0) {
                        errorText = invalidMinutes
                        return@Button
                    }

                    viewModel.setTimer(minutes)

                    onStartTimer()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.start_timer_button))
            }
        }
    }
}

@Composable
private fun PresetButton(
    minutes: Int,
    onClick: () -> Unit
) {
    OutlinedButton(onClick = onClick) {
        Text("$minutes")
    }
}