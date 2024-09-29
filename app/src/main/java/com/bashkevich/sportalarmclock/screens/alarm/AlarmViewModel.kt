package com.bashkevich.sportalarmclock.screens.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import com.bashkevich.sportalarmclock.screens.matches.MatchesScreenState
import com.bashkevich.sportalarmclock.screens.matches.MatchesScreenUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val matchRepository: MatchRepository
) : BaseViewModel<AlarmScreenState, AlarmScreenUiEvent, AlarmScreenAction>() {

    private val reducer = MainReducer(AlarmScreenState.initial())

    override val state: StateFlow<AlarmScreenState>
        get() = reducer.state

    private val matchId = savedStateHandle.get<Int>("MATCH_ID")!!

    init {
        viewModelScope.launch {
            matchRepository.observeMatchById(matchId).collect{match->
                sendEvent(AlarmScreenUiEvent.ShowMatch(match))
            }
        }
    }

    private fun sendEvent(event: AlarmScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: AlarmScreenState) :
        Reducer<AlarmScreenState, AlarmScreenUiEvent>(initial) {
        override fun reduce(oldState: AlarmScreenState, event: AlarmScreenUiEvent) {
            when (event) {
                is AlarmScreenUiEvent.ShowMatch -> {
                    setState(oldState.copy(match = event.match))
                }
                else -> {}
            }
        }
    }

}