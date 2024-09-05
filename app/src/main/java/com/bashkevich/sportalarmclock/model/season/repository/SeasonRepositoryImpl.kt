package com.bashkevich.sportalarmclock.model.season.repository

import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.season.remote.NFLSeasonDto
import com.bashkevich.sportalarmclock.model.season.remote.SeasonDto
import com.bashkevich.sportalarmclock.model.season.remote.SeasonRemoteDataSource

class SeasonRepositoryImpl(
    private val seasonRemoteDataSource: SeasonRemoteDataSource
) : SeasonRepository {

    override suspend fun fetchNHLCurrentSeason(): LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNHLCurrentSeason()


    override suspend fun fetchMLBCurrentSeason():LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchMLBCurrentSeason()

    override suspend fun fetchNBACurrentSeason(): LoadResult<SeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNBACurrentSeason()

    override suspend fun fetchNFLCurrentSeason(): LoadResult<NFLSeasonDto, Throwable> =
        seasonRemoteDataSource.fetchNFLCurrentSeason()

}