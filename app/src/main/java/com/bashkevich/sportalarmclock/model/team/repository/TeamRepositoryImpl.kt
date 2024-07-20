package com.bashkevich.sportalarmclock.model.team.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.doOnSuccess
import com.bashkevich.sportalarmclock.model.network.mapSuccess
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.domain.toDomain
import com.bashkevich.sportalarmclock.model.team.local.TeamLocalDataSource
import com.bashkevich.sportalarmclock.model.team.local.toTeamEntity
import com.bashkevich.sportalarmclock.model.team.remote.TeamRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TeamRepositoryImpl(
    private val teamRemoteDataSource: TeamRemoteDataSource,
    private val teamLocalDataSource: TeamLocalDataSource

) : TeamRepository {

    override suspend fun fetchAllNHLTeams(): LoadResult<List<Team>,Throwable> {

      val result =   teamRemoteDataSource.fetchNHLTeams()
            .mapSuccess { teams -> teams.map { it.toTeamEntity(League.NHL) } }
            .doOnSuccess { teams ->
                teamLocalDataSource.replaceTeamsList(teams)
            }.mapSuccess { teams-> teams.map { it.toDomain() } }
        return result
    }

    override fun observeTeamsByLeagues(leagues: List<League>): Flow<List<Team>> =
        teamLocalDataSource.observeTeamsByLeagues(leagues).map { it.map { it.toDomain() } }

    override suspend fun fetchAllMLBTeams() : LoadResult<List<Team>,Throwable> {

        val result =   teamRemoteDataSource.fetchMLBTeams()
            .mapSuccess { teams -> teams.map { it.toTeamEntity(League.MLB) } }
            .doOnSuccess { teams ->
                teamLocalDataSource.replaceTeamsList(teams)
            }.mapSuccess { teams-> teams.map { it.toDomain() } }
        return result
    }

    override suspend fun fetchAllMLBEspnTeams() : LoadResult<List<Team>,Throwable> {

        val result =   teamRemoteDataSource.fetchMLBEspnTeams()
            .mapSuccess { teams -> teams.map { it.toTeamEntity() } }
            .doOnSuccess { teams ->
                teamLocalDataSource.updateTeamLogos(teams)
            }.mapSuccess { teams-> teams.map { it.toDomain() } }
        return result
    }

    override suspend fun fetchAllNBATeams() : LoadResult<List<Team>,Throwable> {

        val result =   teamRemoteDataSource.fetchNBATeams()
            .mapSuccess { teams -> teams.map { it.toTeamEntity(League.NBA) } }
            .doOnSuccess { teams ->
                teamLocalDataSource.replaceTeamsList(teams)
            }.mapSuccess { teams-> teams.map { it.toDomain() } }
        return result
    }

    override suspend fun fetchAllNFLTeams() : LoadResult<List<Team>,Throwable> {

        val result =   teamRemoteDataSource.fetchNFLTeams()
            .mapSuccess { teams -> teams.map { it.toTeamEntity() } }
            .doOnSuccess { teams ->
                teamLocalDataSource.replaceTeamsList(teams)
            }.mapSuccess { teams-> teams.map { it.toDomain() } }
        return result
    }

    override suspend fun toggleFavouriteSign(teamId: Int, isFavourite: Boolean) {
        teamLocalDataSource.toggleFavouriteSign(teamId = teamId,isFavourite = isFavourite)
    }
}