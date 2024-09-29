package com.bashkevich.sportalarmclock.screens.alarm

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.mvi.UiAction
import com.bashkevich.sportalarmclock.mvi.UiEvent
import com.bashkevich.sportalarmclock.mvi.UiState
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Immutable
sealed class AlarmScreenUiEvent : UiEvent {
    class ShowMatch(val match: Match) : AlarmScreenUiEvent()
}

    private val DEFAULT_MATCH = Match(
        id = 123456,
        leagueType = LeagueType.MLB,
        seasonType = SeasonType.REG,
        homeTeam = Team(
            id = 0,
            leagueType = LeagueType.MLB,
            city = "Test",
            name = "Tigers",
            primaryColor = Color.White,
            logoUrl = "",
            isFavorite = false
        ),
        awayTeam = Team(
            id = 0,
            leagueType = LeagueType.MLB,
            city = "Test",
            name = "Dodgers",
            primaryColor = Color.White,
            logoUrl = "",
            isFavorite = false
        ),
        dateTime = Clock.System.now().plus(2L, DateTimeUnit.MINUTE).toLocalDateTime(
            TimeZone.currentSystemDefault()
        ),
        isChecked = false,
        isAbleToAlarm = true
    )

@Immutable
data class AlarmScreenState(
    val match: Match,
) : UiState {
    companion object {
        fun initial() = AlarmScreenState(
            match = DEFAULT_MATCH
        )
    }

//    override fun toString(): String {
//        //return "isLoading: $isLoading, data.size: ${data.size}, isShowAddDialog: $isShowAddDialog"
//        return "loading State $loadingState "
//    }
}


@Immutable
sealed class AlarmScreenAction : UiAction {

}