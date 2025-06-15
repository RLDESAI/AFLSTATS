package com.rahuldas.aflstats.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    fun createMatch(team1: String, team2: String) {
        val match = hashMapOf("team1" to team1, "team2" to team2, "timestamp" to System.currentTimeMillis())
        db.collection("matches").add(match)
    }

    fun addPlayer(name: String, team1: String, team2: String) {
        val playerData = hashMapOf(
            "name" to name,
            "team" to if (name.contains("1")) team1 else team2,
            "kick" to 0,
            "goal" to 0,
            "tackle" to 0
        )
        db.collection("players").document(name).set(playerData)
    }

    fun recordAction(player: String, action: String) {
        val playerRef = db.collection("players").document(player)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(playerRef)
            val current = snapshot.getLong(action) ?: 0
            transaction.update(playerRef, action, current + 1)
        }
    }

    fun undoAction(player: String, action: String) {
        val playerRef = db.collection("players").document(player)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(playerRef)
            val current = snapshot.getLong(action) ?: 0
            if (current > 0) {
                transaction.update(playerRef, action, current - 1)
            }
        }
    }
}
