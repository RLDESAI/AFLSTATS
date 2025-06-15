package com.rahuldas.aflstats.viewmodel

import androidx.lifecycle.ViewModel
import com.rahuldas.aflstats.repo.FirestoreRepository

class MatchViewModel : ViewModel() {
    var team1: String = ""
    var team2: String = ""
    val players = mutableListOf<String>()
    private val repo = FirestoreRepository()

    // Set teams for the match and add players
    fun setTeams(t1: String, t2: String) {
        team1 = t1
        team2 = t2
        repo.createMatch(team1, team2)
        players.forEach { repo.addPlayer(it, team1, team2) }
    }

    // Add a player to the list
    fun addPlayer(name: String) {
        if (name.isNotBlank()) players.add(name)
    }
}
