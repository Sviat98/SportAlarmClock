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

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertFavTeamSign(favSigns: List<FavouriteTeamEntity>)

    @Query("SELECT * FROM `match` WHERE date_time BETWEEN :dateBegin and :dateEnd ORDER BY date_time")
    fun getAllMatchesByDate(dateBegin: LocalDateTime, dateEnd: LocalDateTime): Flow<List<MatchWithTeamsEntity>>

//    @Query("UPDATE favourite_team SET is_favourite = :isFavourite WHERE team_id = :teamId")
//    suspend fun updateFavTeamSign(teamId: Int, isFavourite: Boolean)

    @Query("DELETE FROM `match` WHERE league = :league")
    suspend fun deleteMatchesByLeague(league: League)

    @Transaction
    suspend fun replaceMatchesList(matches: List<MatchEntity>) {
        val league = matches[0].league

        deleteMatchesByLeague(league)
        insertMatches(matches)
//
//        val teamIds = teams.map { it.id }
//        insertFavTeamSign(teamIds.map { teamId -> FavouriteTeamEntity(teamId, false) })
    }
}