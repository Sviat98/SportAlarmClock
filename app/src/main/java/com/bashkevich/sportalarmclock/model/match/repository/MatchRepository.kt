package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun fetchAllNHLMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllMLBMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllNBAMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>
    suspend fun fetchAllNFLMatches(season: Int, seasonType: SeasonType): LoadResult<Unit, Throwable>

    fun observeMatchesByDate(
        date: LocalDateTime,
        leaguesList: List<LeagueType>,
        teamsMode: TeamsMode
    ): Flow<List<Match>>

    fun observeMatchById(
        matchId: Int,
    ): Flow<Match>

    suspend fun toggleFavouriteSign(matchId: Int,dateTime: LocalDateTime, isFavourite: Boolean)

    suspend fun removeOldMatches(leagueType: LeagueType, season: Int, seasonTypes: List<SeasonType>)
}