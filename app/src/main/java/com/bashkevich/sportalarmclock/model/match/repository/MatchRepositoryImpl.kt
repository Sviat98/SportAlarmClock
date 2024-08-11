package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.League
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
                        league = League.NHL,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches,
                    League.NHL,
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
                        league = League.MLB,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    league = League.MLB,
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
                        league = League.NBA,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    league = League.NBA,
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
            .mapSuccess { matches -> matches.filter { it.status in listOf("Scheduled","InProgress") && it.id != null } }
            .mapSuccess { matches ->
                matches.map {
                    it.toMatchEntity(
                        league = League.NFL,
                        season = season,
                        seasonType = seasonType
                    )
                }
            }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(
                    matches = matches,
                    league = League.NFL,
                    season = season,
                    seasonType = seasonType
                )
            }.mapSuccess { }
        return result
    }

    override fun observeMatchesByDate(
        date: LocalDateTime,
        leaguesList: List<League>,
        teamsMode: TeamsMode
    ) =
        matchLocalDataSource.observeMatchesByDate(date, leaguesList, teamsMode)
            .map { matchEntities ->
                matchEntities.map { it.toDomain() }
            }

    override suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean) {
        matchLocalDataSource.toggleFavouriteSign(matchId = matchId, isFavourite = isFavourite)
    }

    override suspend fun removeOldMatches(
        league: League,
        season: Int,
        seasonTypes: List<SeasonType>
    ) {
        matchLocalDataSource.removeOldMatches(
            league = league,
            season = season,
            seasonTypes = seasonTypes
        )
    }
}