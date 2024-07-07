package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.match.domain.toDomain
import com.bashkevich.sportalarmclock.model.match.local.MatchLocalDataSource
import com.bashkevich.sportalarmclock.model.match.local.toMatchEntity
import com.bashkevich.sportalarmclock.model.match.remote.MatchRemoteDataSource
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.doOnSuccess
import com.bashkevich.sportalarmclock.model.network.mapSuccess
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.domain.toDomain
import com.bashkevich.sportalarmclock.model.team.local.toTeamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class MatchRepositoryImpl(
    private val matchRemoteDataSource: MatchRemoteDataSource,
    private val matchLocalDataSource: MatchLocalDataSource

) : MatchRepository {

    private val _selectedDate = MutableStateFlow(LocalDate(1970, 1, 1))

    override suspend fun fetchAllMLBMatches(season: String): LoadResult<Unit, Throwable> {

        val result = matchRemoteDataSource.fetchMLBMatches(season = season)
            .mapSuccess { matches -> matches.filter { it.status == "Scheduled" } }
            .mapSuccess { matches -> matches.map { it.toMatchEntity(League.MLB) } }
            .doOnSuccess { matches ->
                matchLocalDataSource.replaceMatchesList(matches)
            }.mapSuccess { }
        return result
    }

    override fun observeMatchesByDate(date: LocalDateTime) =
        matchLocalDataSource.observeMatchesByDate(date).map { matchEntities ->
            matchEntities.map { it.toDomain() }
        }

    override fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    override fun observeSelectedDate(): Flow<LocalDate> = _selectedDate.asStateFlow()

}