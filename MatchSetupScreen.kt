package com.rahuldas.aflstats.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rahuldas.aflstats.viewmodel.MatchViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SetupScreen(navController: NavController, viewModel: MatchViewModel = viewModel()) {
    var team1 by remember { mutableStateOf("") }
    var team2 by remember { mutableStateOf("") }
    var player by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Setup Match", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(value = team1, onValueChange = { team1 = it }, label = { Text("Team 1 Name") })
        OutlinedTextField(value = team2, onValueChange = { team2 = it }, label = { Text("Team 2 Name") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = player, onValueChange = { player = it }, label = { Text("Add Player") })
        Button(onClick = {
            if (player.isNotBlank()) {
                viewModel.addPlayer(player)
                player = ""
            }
        }) {
            Text("Add Player")
        }

        Button(
            onClick = {
                if (team1.isBlank() || team2.isBlank()) {
                    // Handle validation for empty teams
                    return@Button
                }
                viewModel.setTeams(team1, team2)
                navController.navigate("match")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Match")
        }
    }
}
