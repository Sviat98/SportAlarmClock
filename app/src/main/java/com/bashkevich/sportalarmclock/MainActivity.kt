package com.bashkevich.sportalarmclock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bashkevich.sportalarmclock.navigation.Alarm
import com.bashkevich.sportalarmclock.navigation.Matches
import com.bashkevich.sportalarmclock.navigation.Settings
import com.bashkevich.sportalarmclock.navigation.Teams
import com.bashkevich.sportalarmclock.screens.alarm.AlarmScreen
import com.bashkevich.sportalarmclock.screens.alarm.AlarmViewModel
import com.bashkevich.sportalarmclock.screens.matches.MatchesScreen
import com.bashkevich.sportalarmclock.screens.matches.MatchesViewModel
import com.bashkevich.sportalarmclock.screens.settings.SettingsScreen
import com.bashkevich.sportalarmclock.screens.settings.SettingsViewModel
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
        startDestination = Matches,
    ) {
        composable<Matches> {
            val viewModel = koinViewModel<MatchesViewModel>()

            val context = LocalContext.current

            MatchesScreen(viewModel = viewModel,
                onTeamsScreenClick = {
                    navController.navigate(route = Teams) //Teams
                },
                onMatchClick = { matchId ->
                    val intent = Intent(context, AlarmActivity::class.java)

                    intent.putExtra("MATCH_ID", matchId)

                    context.startActivity(intent)
                },
                onSettingsScreenClick = { navController.navigate(route = Settings) })
        }
        composable<Teams> {
            val viewModel = koinViewModel<TeamsViewModel>()

            TeamsScreen(viewModel = viewModel, onBack = { navController.navigateUp() })
        }
        composable<Settings> {
            val viewModel = koinViewModel<SettingsViewModel>()

            SettingsScreen(viewModel = viewModel, onBack = { navController.navigateUp() })
        }
    }
}