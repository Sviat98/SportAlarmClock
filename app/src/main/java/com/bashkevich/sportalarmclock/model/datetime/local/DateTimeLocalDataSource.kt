package com.bashkevich.sportalarmclock.model.datetime.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import android.text.format.DateFormat;
import com.bashkevich.sportalarmclock.model.datetime.WESTERN_AMERICA_TIME_ZONE
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus

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

    fun observeTimeFormat() = callbackFlow {
        trySend(DateFormat.is24HourFormat(appContext))
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.action == "android.intent.action.TIME_SET") {
                    trySend(DateFormat.is24HourFormat(appContext))
                }
            }
        }

        appContext.registerReceiver(receiver, IntentFilter("android.intent.action.TIME_SET"))

        awaitClose {
            appContext.unregisterReceiver(receiver)
        }
    }

    fun observeCurrentSystemDate(timeZone: TimeZone) : Flow<LocalDate> = flow {
        while (true){
            emit(Clock.System.now().toLocalDateTime(timeZone).date)
            Log.d("currentSystemDate","${Clock.System.now().toLocalDateTime(timeZone).date}")
            delay(60000L)
        }
    }

    fun observeCurrentPacificSystemDate() : Flow<LocalDate> = flow {
        while (true){
            emit(Clock.System.now().toLocalDateTime(TimeZone.of(WESTERN_AMERICA_TIME_ZONE)).date)
            Log.d("currentSystemDate","${Clock.System.now().toLocalDateTime(TimeZone.of(WESTERN_AMERICA_TIME_ZONE)).date}")
            delay(60000L)
        }
    }

    fun observeCurrentDateTimeMinusHour(timeZone: TimeZone): Flow<LocalDateTime> = flow {
        while (true){
            val localDateTimeMinusHour = Clock.System.now().minus(1, DateTimeUnit.HOUR).toLocalDateTime(timeZone)

            emit(LocalDateTime(localDateTimeMinusHour.date, LocalTime(localDateTimeMinusHour.hour,0,0)))
            Log.d("currentSystemDate","${Clock.System.now().toLocalDateTime(TimeZone.of(WESTERN_AMERICA_TIME_ZONE)).date}")
            delay(60000L)
        }
    }
}