package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.Embedded
import androidx.room.Relation

data class TeamWithFavouriteSignEntity(
    @Embedded val team: TeamEntity,
    @Relation(
    parentColumn = "id",
    entityColumn = "team_id"
)
val favouriteTeamEntity: FavouriteTeamEntity
)
