package com.bashkevich.sportalarmclock.screens.matches

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepository
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import com.bashkevich.sportalarmclock.screens.matches.domain.GetDatesUseCase
import com.bashkevich.sportalarmclock.screens.matches.domain.GetMatchesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class MatchesViewModel(
    private val matchRepository: MatchRepository,
    private val dateTimeRepository: DateTimeRepository,
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getDatesUseCase: GetDatesUseCase
) : BaseViewModel<MatchesScreenState, MatchesScreenUiEvent, MatchesScreenAction>() {

    private val reducer = MainReducer(MatchesScreenState.initial())

    override val state: StateFlow<MatchesScreenState>
        get() = reducer.state


    init {
        viewModelScope.launch {

            getDatesUseCase.execute().collect { dates ->

                Log.d("MatchesViewModel observe dates", "$dates")

                sendEvent(MatchesScreenUiEvent.ShowDates(dates = dates))
            }
        }

        viewModelScope.launch {
            getMatchesUseCase.execute().collect { matches ->
                sendEvent(MatchesScreenUiEvent.ShowMatchesList(matches = matches))
            }
        }

        viewModelScope.launch {
            dateTimeRepository.observeSelectedDate().collect { selectedDate ->
                sendEvent(MatchesScreenUiEvent.SelectDate(selectedDate))
            }
        }

        viewModelScope.launch {
            dateTimeRepository.observeIs24HourFormat().collect { is24HourFormat ->
                sendEvent(MatchesScreenUiEvent.ShowTimeFormat(is24HourFormat))
            }
        }
    }

    private fun sendEvent(event: MatchesScreenUiEvent) {
        reducer.sendEvent(event)
    }

    fun checkFavourite(matchId: Int, dateTime: LocalDateTime, isFavourite: Boolean) {
        viewModelScope.launch {
            matchRepository.toggleFavouriteSign(
                matchId = matchId,
                dateTime = dateTime,
                isFavourite = isFavourite
            )
        }
    }

    fun selectDate(currentTab: LocalDate) {
        dateTimeRepository.selectDate(currentTab)
        Log.d("MatchesViewModel selectDate", "$currentTab")
    }

    private class MainReducer(initial: MatchesScreenState) :
        Reducer<MatchesScreenState, MatchesScreenUiEvent>(initial) {
        override fun reduce(oldState: MatchesScreenState, event: MatchesScreenUiEvent) {
            when (event) {
                is MatchesScreenUiEvent.ShowMatchesList -> {
                    setState(oldState.copy(isLoading = false, matches = event.matches))
                }

                is MatchesScreenUiEvent.ShowDates -> {
                    setState(oldState.copy(dates = event.dates))
                }

                is MatchesScreenUiEvent.ShowTimeFormat -> {
                    setState(oldState.copy(is24HourFormat = event.is24HourFormat))
                }

                is MatchesScreenUiEvent.SelectDate -> {
                    setState(oldState.copy(isLoading = true, selectedDate = event.date))
                }

                else -> {}
            }
        }
    }

}