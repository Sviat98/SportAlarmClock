package com.bashkevich.sportalarmclock.screens.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog

@Composable
fun ScheduleExactAlarmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onOkPressed: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier.background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("You need to enable scheduling exact alarms")
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    onDismissRequest()
                    onOkPressed()
                }) {
                    Text("Go to Settings")
                }
                Button(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        }
    }
}