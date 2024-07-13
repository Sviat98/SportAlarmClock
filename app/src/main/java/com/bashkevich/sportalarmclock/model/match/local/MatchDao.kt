package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bashkevich.sportalarmclock.model.league.League
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(teams: List<MatchEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavMatchSign(favSigns: List<FavouriteMatchEntity>)

    @Query("SELECT * FROM `match` WHERE league in (:leaguesList) and date_time BETWEEN :dateBegin and :dateEnd ORDER BY date_time")
    fun getAllMatchesByDate(leaguesList: List<League>, dateBegin: LocalDateTime, dateEnd: LocalDateTime): Flow<List<MatchWithTeamsEntity>>

    @Query("UPDATE favourite_match SET is_favourite = :isFavourite WHERE match_id = :matchId")
    suspend fun updateFavMatchSign(matchId: Int, isFavourite: Boolean)

    @Query("DELETE FROM `match` WHERE league = :league")
    suspend fun deleteMatchesByLeague(league: League)

    @Transaction
    suspend fun replaceMatchesList(matches: List<MatchEntity>) {
        val league = matches[0].league

        deleteMatchesByLeague(league)
        insertMatches(matches)

        val matchIds = matches.map { it.id }
        insertFavMatchSign(matchIds.map { matchId -> FavouriteMatchEntity(matchId, false) })
    }
}