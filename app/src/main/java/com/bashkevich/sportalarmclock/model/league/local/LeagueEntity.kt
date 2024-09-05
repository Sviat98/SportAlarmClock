package com.bashkevich.sportalarmclock.model.league.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.LeagueType

@Entity(tableName = "league")
data class LeagueEntity(
    @PrimaryKey
    @ColumnInfo(name = "league")
    val leagueType: LeagueType,
    @ColumnInfo(name = "is_followed")
    val followed: Boolean,
    @ColumnInfo(name = "order")
    val order: Int,
)