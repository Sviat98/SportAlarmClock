package com.bashkevich.sportalarmclock.screens.teams

import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepository
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TeamsViewModel(
    private val teamRepository: TeamRepository,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<TeamsScreenState, TeamsScreenUiEvent, TeamsScreenAction>() {

    private val reducer = MainReducer(TeamsScreenState.initial())

    override val state: StateFlow<TeamsScreenState>
        get() = reducer.state

    init {

        viewModelScope.launch {
            settingsRepository.observeLeaguesList().flatMapLatest { leagues->
                teamRepository.observeTeamsByLeagues(leagues).distinctUntilChanged()
            }.collect{ teams->
                sendEvent(TeamsScreenUiEvent.ShowTeamsList(teams))
            }
        }
    }

    private fun sendEvent(event: TeamsScreenUiEvent){
        reducer.sendEvent(event)
    }

    fun checkFavourite(teamId: Int, isFavourite: Boolean) {
        viewModelScope.launch {
            teamRepository.toggleFavouriteSign(teamId = teamId,isFavourite = isFavourite)
        }
    }

    private class MainReducer(initial: TeamsScreenState) :
        Reducer<TeamsScreenState, TeamsScreenUiEvent>(initial) {
        override fun reduce(oldState: TeamsScreenState, event: TeamsScreenUiEvent) {
            when (event) {
                is TeamsScreenUiEvent.ShowTeamsList->{
                    setState(oldState.copy(teams = event.teams))
                }
                else -> {}
            }
        }
    }

}