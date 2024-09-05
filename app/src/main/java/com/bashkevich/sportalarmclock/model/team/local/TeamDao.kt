package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bashkevich.sportalarmclock.model.league.LeagueType
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavTeamSign(favSigns: List<FavouriteTeamEntity>)

    @Query("SELECT * FROM team WHERE league in (:leagueTypes)  and active=1 ORDER BY city,name")
    fun getAllLeagueTeams(leagueTypes: List<LeagueType>): Flow<List<TeamWithFavouriteSignEntity>>

    @Query("UPDATE favourite_team SET is_favourite = :isFavourite WHERE team_id = :teamId")
    suspend fun updateFavTeamSign(teamId: Int, isFavourite: Boolean)

    @Query("DELETE FROM team WHERE league = :leagueType")
    suspend fun deleteTeamsByLeague(leagueType: LeagueType)

    @Query("DELETE FROM favourite_team WHERE team_id NOT IN (:teamIds) and league =:leagueType")
    suspend fun deleteFavTeams(teamIds: List<Int>, leagueType: LeagueType)

    @Transaction
    suspend fun replaceTeamsList(teams: List<TeamEntity>) {
        val league = teams[0].leagueType

        deleteTeamsByLeague(league)
        insertTeams(teams)

        val teamIds = teams.map { it.id }
        deleteFavTeams(teamIds = teamIds, leagueType = league)
        insertFavTeamSign(teamIds.map { teamId -> FavouriteTeamEntity(teamId, league, false) })
    }
}