package com.bashkevich.sportalarmclock.screens.matches

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MatchesViewModel(
    private val matchRepository: MatchRepository,
    private val appContext: Context
) : BaseViewModel<MatchesScreenState, MatchesScreenUiEvent, MatchesScreenAction>() {

    private val reducer = MainReducer(MatchesScreenState.initial())

    override val state: StateFlow<MatchesScreenState>
        get() = reducer.state

    init {

        viewModelScope.launch {
            getTimeZoneFlow().collect { timeZone ->

                val today =
                    Clock.System.now().toLocalDateTime(timeZone).date

                val dates = (0..7).map {
                    today.plus(it, DateTimeUnit.DAY)
                }
                sendEvent(MatchesScreenUiEvent.ShowDates(dates = dates))

                selectDate(today)
            }
        }

        viewModelScope.launch {
            combine(
                getTimeZoneFlow(),
                matchRepository.observeSelectedDate()
            ) { timeZone, currentDate ->
                Pair(currentDate, timeZone)
            }.flatMapLatest {

                val timeZone = it.second
                val currentDate = it.first

                val dateAtLocalMidnight = currentDate.atStartOfDayIn(timeZone).toLocalDateTime(
                    TimeZone.of(AMERICAN_TIME_ZONE)
                )

                matchRepository.observeMatchesByDate(dateAtLocalMidnight)
            }.collect{matches->
                sendEvent(MatchesScreenUiEvent.ShowMatchesList(matches = matches))
            }
        }
    }

    private fun getTimeZoneFlow() = callbackFlow {
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

    private fun sendEvent(event: MatchesScreenUiEvent) {
        reducer.sendEvent(event)
    }

    fun selectDate(currentTab: LocalDate) {
        matchRepository.selectDate(currentTab)
    }

    private class MainReducer(initial: MatchesScreenState) :
        Reducer<MatchesScreenState, MatchesScreenUiEvent>(initial) {
        override fun reduce(oldState: MatchesScreenState, event: MatchesScreenUiEvent) {
            when (event) {
                is MatchesScreenUiEvent.ShowMatchesList -> {
                    setState(oldState.copy(matches = event.matches))
                }

                is MatchesScreenUiEvent.ShowDates -> {
                    setState(oldState.copy(dates = event.dates))
                }

                else -> {}
            }
        }
    }

}