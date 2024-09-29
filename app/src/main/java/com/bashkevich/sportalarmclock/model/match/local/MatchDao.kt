package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.season.SeasonType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface MatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatches(teams: List<MatchEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavMatchSign(favSigns: List<FavouriteMatchEntity>)

    @Query("SELECT * FROM `match` WHERE league in (:leaguesList) and date_time BETWEEN :dateBegin and :dateEnd ORDER BY date_time")
    fun getAllMatchesByDate(
        leaguesList: List<LeagueType>,
        dateBegin: LocalDateTime,
        dateEnd: LocalDateTime
    ): Flow<List<MatchWithTeamsEntity>>

    @Query("SELECT * FROM `match` WHERE id=:matchId")
    fun observeMatchById(
        matchId: Int
    ): Flow<MatchWithTeamsEntity>

    @Query("UPDATE favourite_match SET is_favourite = :isFavourite WHERE match_id = :matchId")
    suspend fun updateFavMatchSign(matchId: Int, isFavourite: Boolean)

    @Query("DELETE FROM `match` WHERE league = :leagueType and season=:season and season_type  =:seasonType")
    suspend fun deleteMatchesByLeagueAndSeason(leagueType: LeagueType, season: Int, seasonType: SeasonType)

    @Query("DELETE FROM favourite_match WHERE match_id NOT IN (:matchIds) and league =:leagueType and season = :season and season_type=:seasonType")
    suspend fun deleteFavMatches(
        matchIds: List<Int>,
        leagueType: LeagueType,
        season: Int,
        seasonType: SeasonType
    )

    @Query("DELETE FROM `match` WHERE league = :leagueType and (season<:season or season_type in (:seasonTypes))")
    suspend fun deleteOldMatches(leagueType: LeagueType, season: Int, seasonTypes: List<SeasonType>)

    @Query("DELETE FROM favourite_match WHERE league = :leagueType and (season<:season or season_type in (:seasonTypes))")
    suspend fun deleteOldFavouriteMatches(
        leagueType: LeagueType,
        season: Int,
        seasonTypes: List<SeasonType>
    )

    @Transaction
    suspend fun removeOldMatches(leagueType: LeagueType, season: Int, seasonTypes: List<SeasonType>) {
        deleteOldMatches(leagueType = leagueType, season = season, seasonTypes = seasonTypes)
        deleteOldFavouriteMatches(leagueType = leagueType, season = season, seasonTypes = seasonTypes)
    }


    @Transaction
    suspend fun replaceMatchesList(
        matches: List<MatchEntity>,
        leagueType: LeagueType,
        season: Int,
        seasonType: SeasonType
    ) {
        deleteMatchesByLeagueAndSeason(leagueType, season, seasonType)
        insertMatches(matches)

        val matchIds = matches.map { it.id }
        deleteFavMatches(
            matchIds = matchIds,
            leagueType = leagueType,
            season = season,
            seasonType = seasonType
        )
        insertFavMatchSign(matchIds.map { matchId ->
            FavouriteMatchEntity(
                matchId,
                leagueType,
                season,
                seasonType,
                false
            )
        })
    }
}