package com.bashkevich.sportalarmclock.model.match.local

import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MatchLocalDataSource(
    private val matchDao: MatchDao
) {
    suspend fun replaceMatchesList(matches: List<MatchEntity>) = withContext(Dispatchers.IO) {
        matchDao.replaceMatchesList(matches)
    }

    fun observeMatchesByDate(date: LocalDateTime): Flow<List<MatchWithTeamsEntity>> {

        val dateEnd = date.toInstant(TimeZone.of(AMERICAN_TIME_ZONE)).plus(
            DateTimePeriod(days = 1), TimeZone.of(
                AMERICAN_TIME_ZONE
            )
        ).toLocalDateTime(TimeZone.of(AMERICAN_TIME_ZONE))
        return matchDao.getAllMatchesByDate(dateBegin = date, dateEnd = dateEnd)

    }
}