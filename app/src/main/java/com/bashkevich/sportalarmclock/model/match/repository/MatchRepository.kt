package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.screens.settings.SettingsScreenUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun fetchAllNHLMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllMLBMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllNBAMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllNFLMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>

    fun observeMatchesByDate(
        date: LocalDateTime,
        leaguesList: List<League>,
        teamsMode: TeamsMode
    ): Flow<List<Match>>

    suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean)

    suspend fun removeOldMatches(league: League, season: Int, seasonTypes: List<SeasonType>)
}