package com.bashkevich.sportalarmclock.model.season.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.season.remote.NFLSeasonDto
import com.bashkevich.sportalarmclock.model.season.remote.SeasonDto
import com.bashkevich.sportalarmclock.model.team.domain.Team
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun fetchNHLCurrentSeason(): LoadResult<SeasonDto, Throwable>
    suspend fun fetchMLBCurrentSeason(): LoadResult<SeasonDto, Throwable>
    suspend fun fetchNBACurrentSeason(): LoadResult<SeasonDto, Throwable>
    suspend fun fetchNFLCurrentSeason(): LoadResult<NFLSeasonDto, Throwable>
}