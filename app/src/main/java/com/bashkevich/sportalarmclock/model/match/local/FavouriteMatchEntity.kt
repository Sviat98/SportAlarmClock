package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_match")
data class FavouriteMatchEntity(
    @PrimaryKey
    @ColumnInfo(name = "match_id")
    val matchId: Int,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean
)