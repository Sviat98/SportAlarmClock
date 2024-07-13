package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.Embedded
import androidx.room.Relation
import com.bashkevich.sportalarmclock.model.match.local.FavouriteMatchEntity

data class MatchWithFavouriteSignEntity(
    @Embedded val matchEntity: MatchEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "match_id"
    )
    val favouriteMatchEntity: FavouriteMatchEntity
)
