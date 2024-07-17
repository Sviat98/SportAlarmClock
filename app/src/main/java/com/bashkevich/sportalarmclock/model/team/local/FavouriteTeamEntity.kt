package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.League

@Entity(tableName = "favourite_team")
data class FavouriteTeamEntity(
    @PrimaryKey
    @ColumnInfo(name = "team_id")
    val teamId: Int,
    @ColumnInfo(name = "league")
    val league: League,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean
)