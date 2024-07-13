package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.bashkevich.sportalarmclock.model.league.League
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavTeamSign(favSigns: List<FavouriteTeamEntity>)

    @Query("SELECT * FROM team WHERE league in (:leagues) ORDER BY city,name")
    fun getAllLeagueTeams(leagues: List<League>): Flow<List<TeamWithFavouriteSignEntity>>

    @Query("UPDATE favourite_team SET is_favourite = :isFavourite WHERE team_id = :teamId")
    suspend fun updateFavTeamSign(teamId: Int, isFavourite: Boolean)

    @Query("DELETE FROM team WHERE league = :league")
    suspend fun deleteTeamsByLeague(league: League)

    @Transaction
    suspend fun replaceTeamsList(teams: List<TeamEntity>) {
        val league = teams[0].league

        deleteTeamsByLeague(league)
        insertTeams(teams)

        val teamIds = teams.map { it.id }
        insertFavTeamSign(teamIds.map { teamId -> FavouriteTeamEntity(teamId, false) })
    }
}