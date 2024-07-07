package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.Embedded
import androidx.room.Relation
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity
import com.bashkevich.sportalarmclock.model.team.local.TeamWithFavouriteSignEntity

data class MatchWithTeamsEntity(
    @Embedded val matchEntity: MatchEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "home_team_id",
        entityColumn = "id"
    )
    val homeTeamEntity: TeamWithFavouriteSignEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "away_team_id",
        entityColumn = "id"
    )
    val awayTeamEntity: TeamWithFavouriteSignEntity
)
