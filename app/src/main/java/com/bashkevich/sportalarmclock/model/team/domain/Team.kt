package com.bashkevich.sportalarmclock.model.team.domain

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity
import com.bashkevich.sportalarmclock.model.team.local.TeamWithFavouriteSignEntity

data class Team(
    val id: Int,
    val league: League,
    val city: String,
    val name: String,
    val logoUrl: String,
    val isFavorite: Boolean
)

fun TeamEntity.toDomain() = Team(id, league, city, name, logoUrl, false)

fun TeamWithFavouriteSignEntity.toDomain() =
    Team(team.id, team.league, team.city, team.name, team.logoUrl, favouriteTeamEntity.isFavourite)