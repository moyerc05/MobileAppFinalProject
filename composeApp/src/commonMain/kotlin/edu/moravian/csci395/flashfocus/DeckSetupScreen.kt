package edu.moravian.csci395.flashfocus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- TEMPORARY DATA MODELS ---
data class Flashcard(val id: Int, var front: String, var back: String)
data class Deck(val id: Int, val name: String, val cards: MutableList<Flashcard>)

@Composable
fun DeckSetupScreen(onStartStudy: () -> Unit) {
    var deckNameInput by remember { mutableStateOf("") }

    // Create a dummy list with one default deck so you can test it immediately
    val decks = remember {
        mutableStateListOf(
            Deck(id = 1, name = "Biology 101", cards = mutableStateListOf())
        )
    }
    var nextDeckId by remember { mutableStateOf(2) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Deck Setup", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // --- DECK CREATION ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = deckNameInput,
                onValueChange = { deckNameInput = it },
                label = { Text("New Deck Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (deckNameInput.isNotBlank()) {
                    decks.add(Deck(nextDeckId++, deckNameInput, mutableStateListOf()))
                    deckNameInput = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // --- LIST OF DECKS ---
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(decks) { deck ->
                DeckItem(
                    deck = deck,
                    onStartStudy = onStartStudy,
                    onDeleteDeck = { decks.remove(deck) }
                )
            }
        }
    }
}

@Composable
fun DeckItem(deck: Deck, onStartStudy: () -> Unit, onDeleteDeck: () -> Unit) {
    // State for the "Add Card" inputs specific to this deck
    var newFront by remember { mutableStateOf("") }
    var newBack by remember { mutableStateOf("") }
    var nextCardId by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Deck Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(deck.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row {
                    Button(onClick = onStartStudy) { Text("Start") }
                    IconButton(onClick = onDeleteDeck) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Deck", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Inline Add Card Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = newFront,
                        onValueChange = { newFront = it },
                        label = { Text("Term") },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = newBack,
                        onValueChange = { newBack = it },
                        label = { Text("Definition") },
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newFront.isNotBlank() && newBack.isNotBlank()) {
                        deck.cards.add(Flashcard(nextCardId++, newFront, newBack))
                        newFront = ""
                        newBack = ""
                    }
                }) {
                    Text("Add Card")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Cards in this Deck
            deck.cards.forEach { card ->
                FlashcardItem(
                    card = card,
                    onDelete = { deck.cards.remove(card) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun FlashcardItem(card: Flashcard, onDelete: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var editFront by remember { mutableStateOf(card.front) }
    var editBack by remember { mutableStateOf(card.back) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditing) {
            // EDIT MODE
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(value = editFront, onValueChange = { editFront = it }, label = { Text("Term") })
                OutlinedTextField(value = editBack, onValueChange = { editBack = it }, label = { Text("Definition") })
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Button(onClick = {
                        card.front = editFront
                        card.back = editBack
                        isEditing = false
                    }) { Text("Save") }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = { isEditing = false }) { Text("Cancel") }
                }
            }
        } else {
            // VIEW MODE
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Front: ${card.front}", fontWeight = FontWeight.Bold)
                    Text("Back: ${card.back}")
                }
                Row {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Card")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Card", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}