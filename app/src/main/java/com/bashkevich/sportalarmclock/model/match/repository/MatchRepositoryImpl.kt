package com.bashkevich.sportalarmclock.model.match.repository

import android.util.Log
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.domain.toDomain
import com.bashkevich.sportalarmclock.model.match.local.MatchLocalDataSource
import com.bashkevich.sportalarmclock.model.match.local.toMatchEntity
import com.bashkevich.sportalarmclock.model.match.remote.MatchRemoteDataSource
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.doOnSuccess
import com.bashkevich.sportalarmclock.model.network.mapSuccess
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class MatchRepositoryImpl(
    private val matchRemoteDataSource: MatchRemoteDataSource,
    private val matchLocalDataSource: MatchLocalDataSource

) : MatchRepository {
    override suspend fun fetchAllNHLMatches(
        season: Int,
        seasonType: SeasonType
    ): LoadResult<Unit, Throwable> {
        val result = matchRemoteDataSource.fetchNHLMatches(season = season, seasonType = seasonType)
            .mapSuccess { matches -> matches.filter { it.status in listOf("Scheduled","InProgress") } }
            .mapSuccess { matches ->
                matches.map {
                    it.toMatchEntity(
                        leagueType = LeagueType.NHL,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches,
                    LeagueType.NHL,
                    season = season,
                    seasonType = seasonType
                )
            }.mapSuccess { }
        return result
    }

    override suspend fun fetchAllMLBMatches(
        season: Int,
        seasonType: SeasonType
    ): LoadResult<Unit, Throwable> {

        val result = matchRemoteDataSource.fetchMLBMatches(season = season, seasonType = seasonType)
            .mapSuccess { matches -> matches.filter { it.status in listOf("Scheduled","InProgress") } }
            .mapSuccess { matches ->
                matches.map {
                    it.toMatchEntity(
                        leagueType = LeagueType.MLB,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    leagueType = LeagueType.MLB,
                    season = season,
                    seasonType = seasonType
                )
            }.mapSuccess { }
        return result
    }

    override suspend fun fetchAllNBAMatches(
        season: Int,
        seasonType: SeasonType
    ): LoadResult<Unit, Throwable> {
        val result = matchRemoteDataSource.fetchNBAMatches(season = season, seasonType = seasonType)
            .mapSuccess { matches -> matches.filter { it.status in listOf("Scheduled","InProgress") } }
            .mapSuccess { matches ->
                matches.map {
                    it.toMatchEntity(
                        leagueType = LeagueType.NBA,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    leagueType = LeagueType.NBA,
                    season = season,
                    seasonType = seasonType
                )
            }.mapSuccess { }
        return result
    }

    override suspend fun fetchAllNFLMatches(
        season: Int,
        seasonType: SeasonType
    ): LoadResult<Unit, Throwable> {
        val result = matchRemoteDataSource.fetchNFLMatches(season = season, seasonType = seasonType)
            .mapSuccess { matches -> matches.filter { it.status in listOf("Scheduled","InProgress") && it.id != null} }
            .mapSuccess { matches ->
                matches.map {
                    it.toMatchEntity(
                        leagueType = LeagueType.NFL,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    leagueType = LeagueType.NFL,
                    season = season,
                    seasonType = seasonType
                )
            }.mapSuccess { }
        return result
    }

    override fun observeMatchesByDate(
        date: LocalDateTime,
        leaguesList: List<LeagueType>,
        teamsMode: TeamsMode
    ) =
        matchLocalDataSource.observeMatchesByDate(date, leaguesList, teamsMode)
            .map { matchEntities ->
                matchEntities.map { it.toDomain() }
            }

    override suspend fun toggleFavouriteSign(matchId: Int,dateTime: LocalDateTime, isFavourite: Boolean) {
        matchLocalDataSource.toggleFavouriteSign(matchId = matchId,dateTime = dateTime, isFavourite = isFavourite)
    }

    override suspend fun removeOldMatches(
        leagueType: LeagueType,
        season: Int,
        seasonTypes: List<SeasonType>
    ) {
        matchLocalDataSource.removeOldMatches(
            leagueType = leagueType,
            season = season,
            seasonTypes = seasonTypes
        )
    }
}