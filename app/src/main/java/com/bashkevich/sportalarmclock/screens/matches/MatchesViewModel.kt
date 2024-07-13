package com.bashkevich.sportalarmclock.screens.matches

import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepository
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.quadruple.Quadruple
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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
    private val dateTimeRepository: DateTimeRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<MatchesScreenState, MatchesScreenUiEvent, MatchesScreenAction>() {

    private val reducer = MainReducer(MatchesScreenState.initial())

    override val state: StateFlow<MatchesScreenState>
        get() = reducer.state

    init {

        viewModelScope.launch {
            dateTimeRepository.observeCurrentTimeZone().collect { timeZone ->

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
                dateTimeRepository.observeCurrentTimeZone(),
                dateTimeRepository.observeSelectedDate(),
                settingsRepository.observeTeamsMode(),
                settingsRepository.observeLeaguesList()
            ) { timeZone, currentDate, teamsMode, leagueList ->
                Quadruple(currentDate, timeZone, teamsMode, leagueList)
            }.flatMapLatest {

                val timeZone = it.second
                val currentDate = it.first

                val teamsMode = it.third

                val leaguesList = it.fourth

                val dateAtLocalMidnight = currentDate.atStartOfDayIn(timeZone).toLocalDateTime(
                    TimeZone.of(AMERICAN_TIME_ZONE)
                )

                matchRepository.observeMatchesByDate(dateAtLocalMidnight,leaguesList, teamsMode)
                    .distinctUntilChanged()
            }.collect { matches ->
                sendEvent(MatchesScreenUiEvent.ShowMatchesList(matches = matches))
            }
        }
    }

    private fun sendEvent(event: MatchesScreenUiEvent) {
        reducer.sendEvent(event)
    }

    fun checkFavourite(matchId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            matchRepository.toggleFavouriteSign(matchId = matchId, isFavourite = isFavourite)
        }
    }

    fun selectDate(currentTab: LocalDate) {
        dateTimeRepository.selectDate(currentTab)
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