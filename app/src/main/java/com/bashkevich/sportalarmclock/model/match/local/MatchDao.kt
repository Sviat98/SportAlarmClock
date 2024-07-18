package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bashkevich.sportalarmclock.model.league.League
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
        leaguesList: List<League>,
        dateBegin: LocalDateTime,
        dateEnd: LocalDateTime
    ): Flow<List<MatchWithTeamsEntity>>

    @Query("UPDATE favourite_match SET is_favourite = :isFavourite WHERE match_id = :matchId")
    suspend fun updateFavMatchSign(matchId: Int, isFavourite: Boolean)

    @Query("DELETE FROM `match` WHERE league = :league and season=:season and season_type  =:seasonType")
    suspend fun deleteMatchesByLeagueAndSeason(league: League, season: Int, seasonType: SeasonType)

    @Query("DELETE FROM favourite_match WHERE match_id NOT IN (:matchIds) and league =:league and season = :season and season_type=:seasonType")
    suspend fun deleteFavMatches(
        matchIds: List<Int>,
        league: League,
        season: Int,
        seasonType: SeasonType
    )

    @Query("DELETE FROM `match` WHERE league = :league and (season<:season or season_type in (:seasonTypes))")
    suspend fun deleteOldMatches(league: League, season: Int, seasonTypes: List<SeasonType>)

    @Query("DELETE FROM favourite_match WHERE league = :league and (season<:season or season_type in (:seasonTypes))")
    suspend fun deleteOldFavouriteMatches(
        league: League,
        season: Int,
        seasonTypes: List<SeasonType>
    )

    @Transaction
    suspend fun removeOldMatches(league: League, season: Int, seasonTypes: List<SeasonType>) {
        deleteOldMatches(league = league, season = season, seasonTypes = seasonTypes)
        deleteOldFavouriteMatches(league = league, season = season, seasonTypes = seasonTypes)
    }


    @Transaction
    suspend fun replaceMatchesList(
        matches: List<MatchEntity>,
        league: League,
        season: Int,
        seasonType: SeasonType
    ) {
        deleteMatchesByLeagueAndSeason(league, season, seasonType)
        insertMatches(matches)

        val matchIds = matches.map { it.id }
        deleteFavMatches(
            matchIds = matchIds,
            league = league,
            season = season,
            seasonType = seasonType
        )
        insertFavMatchSign(matchIds.map { matchId ->
            FavouriteMatchEntity(
                matchId,
                league,
                season,
                seasonType,
                false
            )
        })
    }
}