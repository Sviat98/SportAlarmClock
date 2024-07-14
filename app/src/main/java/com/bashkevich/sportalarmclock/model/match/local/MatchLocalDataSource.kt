package com.bashkevich.sportalarmclock.model.match.local

import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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
    suspend fun replaceMatchesList(matches: List<MatchEntity>,league: League) = withContext(Dispatchers.IO) {
            matchDao.replaceMatchesList(matches,league)
    }

    fun observeMatchesByDate(date: LocalDateTime,leaguesList: List<League>, teamsMode: TeamsMode): Flow<List<MatchWithTeamsEntity>> {

        val dateEnd = date.toInstant(TimeZone.of(AMERICAN_TIME_ZONE)).plus(
            DateTimePeriod(days = 1), TimeZone.of(
                AMERICAN_TIME_ZONE
            )
        ).toLocalDateTime(TimeZone.of(AMERICAN_TIME_ZONE))
        return matchDao.getAllMatchesByDate(leaguesList = leaguesList,dateBegin = date, dateEnd = dateEnd).map { matches->
            matches.filter {match->
                if(teamsMode == TeamsMode.FAVOURITES) {
                    match.homeTeamEntity.favouriteTeamEntity.isFavourite || match.awayTeamEntity.favouriteTeamEntity.isFavourite
                }else {
                    true
                }
            }
        }

    }

    suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            matchDao.updateFavMatchSign(matchId = matchId, isFavourite = isFavourite)
        }
}