package com.bashkevich.sportalarmclock.model.team.domain

import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity
import com.bashkevich.sportalarmclock.model.team.local.TeamWithFavouriteSignEntity

data class Team(
    val id: Int,
    val leagueType: LeagueType,
    val city: String,
    val name: String,
    val logoUrl: String,
    val isFavorite: Boolean
)

fun TeamEntity.toDomain() = Team(id, leagueType, city, name, logoUrl, false)

fun TeamWithFavouriteSignEntity.toDomain() =
    Team(team.id, team.leagueType, team.city, team.name, team.logoUrl, favouriteTeamEntity.isFavourite)