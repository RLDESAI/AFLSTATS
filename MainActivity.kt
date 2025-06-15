package com.rahuldas.aflstats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahuldas.aflstats.ui.MatchScreen
import com.rahuldas.aflstats.ui.SetupScreen
import com.rahuldas.aflstats.ui.theme.AFLStatsTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            AFLStatsTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "setup") {
                    composable("setup") { SetupScreen(navController) }
                    composable("match") { MatchScreen(navController) }
                }
            }
        }
    }
}
