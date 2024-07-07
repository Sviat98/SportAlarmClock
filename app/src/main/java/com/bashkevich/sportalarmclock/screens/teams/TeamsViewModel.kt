package com.bashkevich.sportalarmclock.screens.teams

import androidx.lifecycle.viewModelScope
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import com.bashkevich.sportalarmclock.mvi.BaseViewModel
import com.bashkevich.sportalarmclock.mvi.Reducer
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class TeamsViewModel(
    private val teamRepository: TeamRepository
) : BaseViewModel<TeamsScreenState, TeamsScreenUiEvent, TeamsScreenAction>() {

    private val reducer = MainReducer(TeamsScreenState.initial())

    override val state: StateFlow<TeamsScreenState>
        get() = reducer.state

    init {

        viewModelScope.launch {
            teamRepository.observeTeamsByLeague(League.NHL).distinctUntilChanged().collect{ teams->
                sendEvent(TeamsScreenUiEvent.ShowNHLTeamsList(teams))
            }
        }

        viewModelScope.launch {
            teamRepository.observeTeamsByLeague(League.MLB).distinctUntilChanged().collect{ teams->
                sendEvent(TeamsScreenUiEvent.ShowMLBTeamsList(teams))
            }
        }

        viewModelScope.launch {
            teamRepository.observeTeamsByLeague(League.NBA).distinctUntilChanged().collect{ teams->
                sendEvent(TeamsScreenUiEvent.ShowNBATeamsList(teams))
            }
        }

        viewModelScope.launch {
            teamRepository.observeTeamsByLeague(League.NFL).distinctUntilChanged().collect{ teams->
                sendEvent(TeamsScreenUiEvent.ShowNFLTeamsList(teams))
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
                is TeamsScreenUiEvent.ShowNHLTeamsList->{
                    setState(oldState.copy(nhlTeams = event.teams))
                }
                is TeamsScreenUiEvent.ShowMLBTeamsList->{
                    setState(oldState.copy(mlbTeams = event.teams))
                }
                is TeamsScreenUiEvent.ShowNBATeamsList->{
                    setState(oldState.copy(nbaTeams = event.teams))
                }
                is TeamsScreenUiEvent.ShowNFLTeamsList->{
                    setState(oldState.copy(nflTeams = event.teams))
                }
                else -> {}
            }
        }
    }

}