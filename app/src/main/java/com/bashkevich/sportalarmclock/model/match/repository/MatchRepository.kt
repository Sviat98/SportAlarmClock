package com.bashkevich.sportalarmclock.model.match.repository

import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.network.LoadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface MatchRepository {
    suspend fun fetchAllMLBMatches(season: String): LoadResult<Unit, Throwable>
    fun observeMatchesByDate(date: LocalDateTime): Flow<List<Match>>
    fun selectDate(date: LocalDate)
    fun observeSelectedDate() : Flow<LocalDate>
}