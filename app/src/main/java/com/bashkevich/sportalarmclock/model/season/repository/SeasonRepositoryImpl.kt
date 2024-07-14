package com.bashkevich.sportalarmclock.model.season.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.doOnSuccess
import com.bashkevich.sportalarmclock.model.network.mapSuccess
import com.bashkevich.sportalarmclock.model.season.remote.SeasonDto
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.domain.toDomain
import com.bashkevich.sportalarmclock.model.team.local.TeamLocalDataSource
import com.bashkevich.sportalarmclock.model.team.local.toTeamEntity
import com.bashkevich.sportalarmclock.model.season.remote.SeasonRemoteDataSource
import com.bashkevich.sportalarmclock.model.season.repository.SeasonRepository
import kotlinx.coroutines.flow.map

class SeasonRepositoryImpl(
    private val seasonRemoteDataSource: SeasonRemoteDataSource
) : SeasonRepository {

    override suspend fun fetchNHLCurrentSeason(): LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNHLCurrentSeason()


    override suspend fun fetchMLBCurrentSeason():LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchMLBCurrentSeason()

    override suspend fun fetchNBACurrentSeason(): LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNBACurrentSeason()

    override suspend fun fetchNFLCurrentSeason(): LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNFLCurrentSeason()

}