package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.domain.toDomain
import com.bashkevich.sportalarmclock.model.match.local.MatchLocalDataSource
import com.bashkevich.sportalarmclock.model.match.local.toMatchEntity
import com.bashkevich.sportalarmclock.model.match.remote.MatchRemoteDataSource
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.doOnSuccess
import com.bashkevich.sportalarmclock.model.network.mapSuccess
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class MatchRepositoryImpl(
    private val matchRemoteDataSource: MatchRemoteDataSource,
    private val matchLocalDataSource: MatchLocalDataSource

) : MatchRepository {


    override suspend fun fetchAllMLBMatches(season: String): LoadResult<Unit, Throwable> {

        val result = matchRemoteDataSource.fetchMLBMatches(season = season)
            .mapSuccess { matches -> matches.filter { it.status == "Scheduled" } }
            .mapSuccess { matches -> matches.map { it.toMatchEntity(League.MLB) } }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(matches)
            }.mapSuccess { }
        return result
    }

    override fun observeMatchesByDate(date: LocalDateTime, leaguesList: List<League>, teamsMode: TeamsMode) =
        matchLocalDataSource.observeMatchesByDate(date,leaguesList, teamsMode).map { matchEntities ->
            matchEntities.map { it.toDomain() }
        }

    override suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean) {
        matchLocalDataSource.toggleFavouriteSign(matchId = matchId, isFavourite = isFavourite)
    }


}