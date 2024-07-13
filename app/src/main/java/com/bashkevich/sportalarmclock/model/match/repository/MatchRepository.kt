package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.screens.settings.SettingsScreenUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun fetchAllMLBMatches(season: String): LoadResult<Unit, Throwable>
    fun observeMatchesByDate(date: LocalDateTime,leaguesList: List<League>, teamsMode: TeamsMode): Flow<List<Match>>
    suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean)

}