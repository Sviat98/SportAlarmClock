package com.bashkevich.sportalarmclock

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.bashkevich.sportalarmclock.screens.dialog.ScheduleExactAlarmDialog
import com.bashkevich.sportalarmclock.screens.matches.MatchesScreen
import com.bashkevich.sportalarmclock.screens.matches.MatchesViewModel
import com.bashkevich.sportalarmclock.screens.settings.SettingsScreen
import com.bashkevich.sportalarmclock.screens.settings.SettingsViewModel
import com.bashkevich.sportalarmclock.screens.teams.TeamsScreen
import com.bashkevich.sportalarmclock.screens.teams.TeamsViewModel
import com.bashkevich.sportalarmclock.ui.theme.SportAlarmClockTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
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


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SportAlarmClockHavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()


    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    SportAlarmClockLifecycleObserver(lifecycleOwner = lifecycleOwner) { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d("MainActivity", "onResume callback")

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

                val currentBackStackEntry = navController.currentBackStackEntry

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager!!.canScheduleExactAlarms() && currentBackStackEntry?.destination?.route != Screens.ScheduleExactAlarmDialog.route) {
                    // If not, request the SCHEDULE_EXACT_ALARM permission
                    navController.navigate(Screens.ScheduleExactAlarmDialog.route)
                }
            }

            else -> {}
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.Matches.route,
    ) {
        composable(route = Screens.Matches.route) {
            val viewModel = koinViewModel<MatchesViewModel>()

            MatchesScreen(viewModel = viewModel, onTeamsScreenClick = {
                navController.navigate(route = Screens.Teams.route)
            }, onSettingsScreenClick = { navController.navigate(route = Screens.Settings.route) })
        }
        dialog(route = Screens.ScheduleExactAlarmDialog.route) {
            ScheduleExactAlarmDialog(
                onDismissRequest = { navController.navigateUp() },
                onOkPressed = {
                    val intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.setData(Uri.fromParts("package", context.packageName, null))

                    context.startActivity(intent)
                })
        }
        composable(route = Screens.Teams.route) {
            val viewModel = koinViewModel<TeamsViewModel>()

            TeamsScreen(viewModel = viewModel, onBack = { navController.navigateUp() })
        }
        composable(route = Screens.Settings.route) {
            val viewModel = koinViewModel<SettingsViewModel>()

            SettingsScreen(viewModel = viewModel, onBack = { navController.navigateUp() })
        }
    }
}

@Composable
fun SportAlarmClockLifecycleObserver(
    lifecycleOwner: LifecycleOwner,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


sealed class Screens(
    val route: String,
) {
    data object Matches : Screens("matches")
    data object ScheduleExactAlarmDialog : Screens("schedule_exact_alarm")
    data object Teams : Screens("teams")
    data object Settings : Screens("settings")
}