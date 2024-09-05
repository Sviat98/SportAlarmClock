package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.season.SeasonType

@Entity(tableName = "favourite_match")
data class FavouriteMatchEntity(
    @PrimaryKey
    @ColumnInfo(name = "match_id")
    val matchId: Int,
    @ColumnInfo(name = "league")
    val leagueType: LeagueType,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "season_type")
    val seasonType: SeasonType,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean
)