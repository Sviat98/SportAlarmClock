package com.bashkevich.sportalarmclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bashkevich.sportalarmclock.screens.alarm.AlarmScreen
import com.bashkevich.sportalarmclock.screens.alarm.AlarmScreenNew
import com.bashkevich.sportalarmclock.screens.alarm.AlarmViewModel
import com.bashkevich.sportalarmclock.ui.theme.SportAlarmClockTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlarmActivity : ComponentActivity() {
    private val alarmViewModel by viewModel<AlarmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SportAlarmClockTheme {
                AlarmScreen(
                    viewModel = alarmViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SportAlarmClockTheme {
        Greeting("Android")
    }
}