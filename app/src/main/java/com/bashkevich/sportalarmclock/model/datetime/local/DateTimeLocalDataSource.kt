package com.bashkevich.sportalarmclock.model.datetime.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.datetime.TimeZone

class DateTimeLocalDataSource(
    private val appContext: Context
) {

    fun observeCurrentTimeZone() = callbackFlow {
        trySend(TimeZone.currentSystemDefault())
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
                    trySend(TimeZone.currentSystemDefault())
                }
            }
        }

        appContext.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))

        awaitClose {
            appContext.unregisterReceiver(receiver)
        }
    }
}