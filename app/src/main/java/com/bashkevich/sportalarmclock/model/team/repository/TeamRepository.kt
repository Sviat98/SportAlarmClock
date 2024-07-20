package com.bashkevich.sportalarmclock.model.team.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.team.domain.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    suspend fun fetchAllNHLTeams(): LoadResult<List<Team>, Throwable>
    suspend fun fetchAllMLBTeams(): LoadResult<List<Team>, Throwable>
    fun observeTeamsByLeagues(leagues: List<League>): Flow<List<Team>>
    suspend fun toggleFavouriteSign(teamId: Int, isFavourite: Boolean)
    suspend fun fetchAllNBATeams(): LoadResult<List<Team>, Throwable>
    suspend fun fetchAllNFLTeams(): LoadResult<List<Team>, Throwable>
}