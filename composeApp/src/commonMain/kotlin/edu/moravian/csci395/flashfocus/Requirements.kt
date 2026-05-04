package edu.moravian.csci395.flashfocus


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// A simple data structure to hold your timer requirements
data class StudyRequirement(val id: Int, val title: String, val durationMinutes: Int)

@Composable
fun RequirementsScreen(
    onStartTimer: () -> Unit, // Triggered when they click "Start" on a requirement
) {
    // State variables to hold the user's typed input
    var titleInput by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("") }

    // Our temporary "database" list. Using mutableStateListOf means the UI
    // will automatically update whenever an item is added.
    val requirementsList = remember { mutableStateListOf<StudyRequirement>() }
    var nextId by remember { mutableStateOf(1) } // Fake ID generator for the mock database

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Study Requirements", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // --- INPUT SECTION ---
        OutlinedTextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            label = { Text("What are you studying?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = timeInput,
            onValueChange = { timeInput = it },
            label = { Text("Duration (minutes)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Convert text to integer, default to 0 if invalid
                val duration = timeInput.toIntOrNull() ?: 0

                // Only save if the fields aren't empty/invalid
                if (titleInput.isNotBlank() && duration > 0) {
                    requirementsList.add(StudyRequirement(nextId++, titleInput, duration))
                    // Clear the fields after saving
                    titleInput = ""
                    timeInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save to Database")
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // --- LIST SECTION ---
        Text(
            text = "Saved Requirements",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        // LazyColumn only renders the items currently visible on screen (great for performance)
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
            items(requirementsList) { requirement ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(requirement.title, fontWeight = FontWeight.Bold)
                            Text("${requirement.durationMinutes} mins")
                        }
                        Button(onClick = onStartTimer) {
                            Text("Start")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}