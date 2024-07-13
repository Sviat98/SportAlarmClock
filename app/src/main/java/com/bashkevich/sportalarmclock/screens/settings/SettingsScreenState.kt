package com.bashkevich.sportalarmclock.screens.settings

import androidx.compose.runtime.Immutable
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState

@Immutable
sealed class SettingsScreenUiEvent : UiEvent {
    class ShowLeaguesList(val leagues: List<League>) : SettingsScreenUiEvent()
    class ShowTeamsMode(val teamsMode: TeamsMode) : SettingsScreenUiEvent()
}

@Immutable
data class SettingsScreenState(
    val leagues: List<League>,
    val teamsMode: TeamsMode
) : UiState {
    companion object {

        fun initial() = SettingsScreenState(
            leagues = emptyList(),
            teamsMode = TeamsMode.ALL
        )
    }

//    override fun toString(): String {
//        //return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
//        return "loading State $loadingState "
//    }
}


@Immutable
sealed class SettingsScreenAction : UiAction {

}