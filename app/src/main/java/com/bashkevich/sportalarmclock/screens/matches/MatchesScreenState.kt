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
    class ShowDates(val dates: List<Pair<LocalDate,Boolean>>) : MatchesScreenUiEvent()
    class ShowTimeFormat(val is24HourFormat: Boolean) : MatchesScreenUiEvent()
    class SelectDate(val date: LocalDate) : MatchesScreenUiEvent()
}

@Immutable
data class MatchesScreenState(
    val isLoading: Boolean,
    val dates: List<Pair<LocalDate,Boolean>>,
    val selectedDate: LocalDate,
    val is24HourFormat: Boolean,
    val matches: List<Match>,
) : UiState {
    companion object {
        fun initial() = MatchesScreenState(
            isLoading = true,
            dates = emptyList(),
            selectedDate = LocalDate(1970,1,1),
            is24HourFormat = true,
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