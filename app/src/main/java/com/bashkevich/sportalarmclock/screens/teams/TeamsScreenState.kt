package com.bashkevich.sportalarmclock.screens.teams

import androidx.compose.runtime.Immutable
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState

@Immutable
sealed class TeamsScreenUiEvent : UiEvent {
    class ShowNHLTeamsList(val teams: List<Team>) : TeamsScreenUiEvent()
    class ShowMLBTeamsList(val teams: List<Team>) : TeamsScreenUiEvent()
    class ShowNBATeamsList(val teams: List<Team>) : TeamsScreenUiEvent()
    class ShowNFLTeamsList(val teams: List<Team>) : TeamsScreenUiEvent()
}

@Immutable
data class TeamsScreenState(
    val nhlTeams: List<Team>,
    val mlbTeams: List<Team>,
    val nbaTeams: List<Team>,
    val nflTeams: List<Team>
) : UiState {
    companion object {
        fun initial() = TeamsScreenState(
            nhlTeams = emptyList(),
            mlbTeams = emptyList(),
            nbaTeams = emptyList(),
            nflTeams = emptyList()
        )
    }

//    override fun toString(): String {
//        //return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
//        return "loading State $loadingState "
//    }
}


@Immutable
sealed class TeamsScreenAction : UiAction {

}
