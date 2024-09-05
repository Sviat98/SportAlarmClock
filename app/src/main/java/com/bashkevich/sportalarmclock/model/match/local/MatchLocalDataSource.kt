package com.bashkevich.sportalarmclock.model.match.local

import android.util.Log
import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    suspend fun replaceMatchesList(
        matches: List<MatchEntity>,
        leagueType: LeagueType,
        season: Int,
        seasonType: SeasonType
    ) =
        withContext(Dispatchers.IO) {
            matchDao.replaceMatchesList(matches, leagueType, season, seasonType)
        }

    fun observeMatchesByDate(
        date: LocalDateTime,
        leaguesList: List<LeagueType>,
        teamsMode: TeamsMode
    ): Flow<List<MatchWithTeamsEntity>> {

        val dateEnd = date.toInstant(TimeZone.of(AMERICAN_TIME_ZONE)).plus(
            DateTimePeriod(hours = 23, minutes = 59, seconds = 59), TimeZone.of(
                AMERICAN_TIME_ZONE
            )
        ).toLocalDateTime(TimeZone.of(AMERICAN_TIME_ZONE))

        Log.d("MatchLocalDataSource dates","$date $dateEnd")
        return matchDao.getAllMatchesByDate(
            leaguesList = leaguesList,
            dateBegin = date,
            dateEnd = dateEnd
        ).map { matches ->
            Log.d("MatchLocalDataSource matches",matches.toString())

            matches.filter { match ->
                if (teamsMode == TeamsMode.FAVOURITES) {
                    match.homeTeamEntity.favouriteTeamEntity.isFavourite || match.awayTeamEntity.favouriteTeamEntity.isFavourite
                } else {
                    true
                }
            }
        }

    }

    suspend fun toggleFavouriteSign(matchId: Int, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            matchDao.updateFavMatchSign(matchId = matchId, isFavourite = isFavourite)
        }

    suspend fun removeOldMatches(leagueType: LeagueType, season: Int, seasonTypes: List<SeasonType>) =
        withContext(Dispatchers.IO) {
            matchDao.removeOldMatches(leagueType = leagueType, season = season, seasonTypes = seasonTypes)
        }
}