package com.bashkevich.sportalarmclock.screens.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : BaseViewModel<SettingsScreenState, SettingsScreenUiEvent, SettingsScreenAction>() {

    private val reducer = MainReducer(SettingsScreenState.initial())

    override val state: StateFlow<SettingsScreenState>
        get() = reducer.state

    init {
        viewModelScope.launch {
            settingsRepository.observeLeaguesList().collect { leagues ->
                sendEvent(SettingsScreenUiEvent.ShowLeaguesList(leagues = leagues))
            }
        }

        viewModelScope.launch {
            settingsRepository.observeTeamsMode().collect { teamsMode ->
                sendEvent(SettingsScreenUiEvent.ShowTeamsMode(teamsMode = teamsMode))
            }
        }
    }

    private fun sendEvent(event: SettingsScreenUiEvent) {
        reducer.sendEvent(event)
    }

    fun toggleLeague(league: League, checked: Boolean) {
        Log.d("leagues checked", checked.toString())

        viewModelScope.launch {
            val leagues = state.value.leagues.toMutableList()

            if (checked) {
                leagues += league
            } else {
                leagues -= league
            }

            Log.d("leagues", leagues.joinToString(","))

            settingsRepository.setLeaguesList(leagues)
        }

    }

    fun toggleTeamMode(teamsMode: TeamsMode) {
       viewModelScope.launch {
           settingsRepository.setTeamsMode(teamsMode = teamsMode)
       }
    }

    private class MainReducer(initial: SettingsScreenState) :
        Reducer<SettingsScreenState, SettingsScreenUiEvent>(initial) {
        override fun reduce(oldState: SettingsScreenState, event: SettingsScreenUiEvent) {
            when (event) {
                is SettingsScreenUiEvent.ShowLeaguesList -> {
                    setState(oldState.copy(leagues = event.leagues))
                }

                is SettingsScreenUiEvent.ShowTeamsMode -> {
                    setState(oldState.copy(teamsMode = event.teamsMode))
                }
                else -> {}
            }
        }
    }

}