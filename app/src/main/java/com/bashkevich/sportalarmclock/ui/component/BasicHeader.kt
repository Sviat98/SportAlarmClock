package com.bashkevich.sportalarmclock.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BasicHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier
    ) {
        Text(text = text, modifier = Modifier.fillMaxWidth())

    }
}