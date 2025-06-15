package com.rahuldas.aflstats.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rahuldas.aflstats.repo.FirestoreRepository
import com.rahuldas.aflstats.viewmodel.MatchViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MatchScreen(navController: NavController, viewModel: MatchViewModel = viewModel()) {
    val repo = FirestoreRepository()

    // Player selection and action tracking
    var selectedPlayer by remember { mutableStateOf("") }
    var lastAction by remember { mutableStateOf("") }
    var playerStats by remember { mutableStateOf(PlayerStats(0, 0, 0)) }

    // Ensure players are available from the view model
    val players = viewModel.players
    val team1 = viewModel.team1
    val team2 = viewModel.team2

    // Check if players are available
    if (players.isEmpty()) {
        // Handle empty player list
        Text("No players available. Please add players.")
        return
    }

    // Column Layout for the MatchScreen
    Column(modifier = Modifier.padding(16.dp)) {
        // Match Information Header
        Text("Match: $team1 vs $team2", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(8.dp))
        Text("Players:")

        // Displaying Players List with Buttons for Each Player
        LazyColumn {
            items(players) { player ->
                Button(onClick = { selectedPlayer = player }, modifier = Modifier.padding(4.dp)) {
                    Text(player)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Actions:")

        // Action Buttons (Kick, Goal, Tackle)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("kick", "goal", "tackle").forEach { action ->
                Button(onClick = {
                    // Validate action, ensure goal follows kick
                    if (action == "goal" && lastAction != "kick") return@Button
                    repo.recordAction(selectedPlayer, action)
                    lastAction = action
                }) {
                    Text(action.capitalize())
                }
            }
        }

        // Undo Button to Revert Last Action
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (lastAction.isNotEmpty()) {
                repo.undoAction(selectedPlayer, lastAction)
                lastAction = ""
            }
        }) {
            Text("Undo")
        }

        // Display Live Stats for Selected Player
        Spacer(Modifier.height(16.dp))
        Text("Live Stats:")
        Text("Kicks: ${playerStats.kick}")
        Text("Goals: ${playerStats.goal}")
        Text("Tackles: ${playerStats.tackle}")
    }

    // Real-time updates from Firestore and ViewModel
    LaunchedEffect(selectedPlayer) {
        val db = FirebaseFirestore.getInstance()
        db.collection("players").document(selectedPlayer).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val stats = snapshot.get("stats") as? Map<String, Any> ?: return@addSnapshotListener
                playerStats = PlayerStats(
                    stats["kick"] as? Int ?: 0,
                    stats["goal"] as? Int ?: 0,
                    stats["tackle"] as? Int ?: 0
                )
            }
        }
    }
}
