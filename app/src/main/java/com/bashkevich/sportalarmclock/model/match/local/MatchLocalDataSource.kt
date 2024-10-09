package com.bashkevich.sportalarmclock.model.match.local

import android.util.Log
import com.bashkevich.sportalarmclock.alarm.AlarmScheduler
import com.bashkevich.sportalarmclock.model.datetime.EASTERN_AMERICA_TIME_ZONE
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class MatchLocalDataSource(
    private val matchDao: MatchDao,
    private val alarmScheduler: AlarmScheduler
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
        dateTimeBegin: LocalDateTime,
        dateTimeEnd: LocalDateTime,
        leaguesList: List<LeagueType>,
        teamsMode: TeamsMode
    ): Flow<List<MatchWithTeamsEntity>> {



        Log.d("MatchLocalDataSource dates","$dateTimeBegin $dateTimeEnd")
        return matchDao.getAllMatchesByDate(
            leaguesList = leaguesList,
            dateBegin = dateTimeBegin,
            dateEnd = dateTimeEnd
        ).map { matches ->
            Log.d("MatchLocalDataSource matches",matches.toString())

            matches.filter { match ->
                if (teamsMode == TeamsMode.FAVOURITES) {
                    match.homeTeamEntity.favouriteTeamEntity.isFavourite || match.awayTeamEntity.favouriteTeamEntity.isFavourite
                } else {
                    true
                }
            }
        }.onEach {  }

    }

    suspend fun toggleFavouriteSign(matchId: Int,dateTime: LocalDateTime, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            matchDao.updateFavMatchSign(matchId = matchId, isFavourite = isFavourite)
            if(isFavourite){
                alarmScheduler.schedule(matchId = matchId, dateTime = dateTime)
            }else{
                alarmScheduler.cancel(matchId = matchId)
            }

        }

    suspend fun removeOldMatches(leagueType: LeagueType, season: Int, seasonTypes: List<SeasonType>) =
        withContext(Dispatchers.IO) {
            matchDao.removeOldMatches(leagueType = leagueType, season = season, seasonTypes = seasonTypes)
        }

    fun observeMatchById(matchId: Int): Flow<MatchWithTeamsEntity> =
        matchDao.observeMatchById(matchId = matchId)
}