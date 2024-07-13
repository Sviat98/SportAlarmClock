package com.bashkevich.sportalarmclock.screens.matches

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
sealed class MatchesScreenUiEvent : UiEvent {
    class ShowMatchesList(val matches: List<Match>) : MatchesScreenUiEvent()
    class ShowDates(val dates: List<LocalDate>) : MatchesScreenUiEvent()
}

@Immutable
data class MatchesScreenState(
    val dates: List<LocalDate>,
    val matches: List<Match>,
) : UiState {
    companion object {
        private val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        fun initial() = MatchesScreenState(
            dates = emptyList(),
            matches = emptyList()
        )
    }

//    override fun toString(): String {
//        //return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
//        return "loading State $loadingState "
//    }
}


@Immutable
sealed class MatchesScreenAction : UiAction {

}