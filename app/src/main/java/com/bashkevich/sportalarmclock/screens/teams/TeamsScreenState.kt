package com.bashkevich.sportalarmclock.screens.teams

import androidx.compose.runtime.Immutable
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState

@Immutable
sealed class TeamsScreenUiEvent : UiEvent {
    class ShowTeamsList(val teams: List<Team>) : TeamsScreenUiEvent()
}

@Immutable
data class TeamsScreenState(
    val teams: List<Team>,
) : UiState {
    companion object {
        fun initial() = TeamsScreenState(
            teams = emptyList()
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
