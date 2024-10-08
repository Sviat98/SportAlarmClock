package com.bashkevich.sportalarmclock.screens.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.di.teamModule
import com.bashkevich.sportalarmclock.model.league.LeagueType
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
            settingsRepository.observeSettingsData().collect { settingsData ->
                sendEvent(SettingsScreenUiEvent.ShowSettingsData(leagueTypes = settingsData.leagueList, teamsMode = settingsData.teamsMode))
            }
        }
    }

    private fun sendEvent(event: SettingsScreenUiEvent) {
        reducer.sendEvent(event)
    }

    fun toggleLeague(leagueType: LeagueType, checked: Boolean) {
        Log.d("leagues checked", checked.toString())

        viewModelScope.launch {
            val leagues = state.value.leagueTypes.toMutableList()

            if (checked) {
                leagues += leagueType
            } else {
                leagues -= leagueType
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
                is SettingsScreenUiEvent.ShowSettingsData -> {
                    setState(oldState.copy(leagueTypes = event.leagueTypes, teamsMode = event.teamsMode))
                }
                else -> {}
            }
        }
    }

}