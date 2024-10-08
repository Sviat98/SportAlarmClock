package com.bashkevich.sportalarmclock.screens.settings

import androidx.compose.runtime.Immutable
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState

@Immutable
sealed class SettingsScreenUiEvent : UiEvent {
    class ShowSettingsData(val leagueTypes: List<LeagueType>,val teamsMode: TeamsMode) : SettingsScreenUiEvent()
}

@Immutable
data class SettingsScreenState(
    val leagueTypes: List<LeagueType>,
    val teamsMode: TeamsMode
) : UiState {
    companion object {

        fun initial() = SettingsScreenState(
            leagueTypes = emptyList(),
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