package com.bashkevich.sportalarmclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bashkevich.sportalarmclock.screens.matches.MatchesScreen
import com.bashkevich.sportalarmclock.screens.matches.MatchesViewModel
import com.bashkevich.sportalarmclock.screens.teams.TeamsScreen
import com.bashkevich.sportalarmclock.screens.teams.TeamsViewModel
import com.bashkevich.sportalarmclock.ui.theme.SportAlarmClockTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportAlarmClockTheme {
                SportAlarmClockHavHost(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Composable
fun SportAlarmClockHavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Matches.route,
    ) {
        composable(route = Screens.Matches.route) {
            val viewModel = koinViewModel<MatchesViewModel>()

            MatchesScreen(viewModel = viewModel, onTeamsScreenClick = {
                navController.navigate(route = Screens.Teams.route)
            })
        }
        composable(route = Screens.Teams.route) {
            val viewModel = koinViewModel<TeamsViewModel>()

            TeamsScreen(viewModel = viewModel, onBack = { navController.navigateUp() })
        }
    }
}


sealed class Screens(
    val route: String,
) {
    data object Matches : Screens("matches")

    data object Teams : Screens("teams")
}