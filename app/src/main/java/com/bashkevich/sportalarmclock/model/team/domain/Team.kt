package com.bashkevich.sportalarmclock.model.team.domain

import androidx.compose.ui.graphics.Color
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity
import com.bashkevich.sportalarmclock.model.team.local.TeamWithFavouriteSignEntity

data class Team(
    val id: Int,
    val leagueType: LeagueType,
    val city: String,
    val name: String,
    val primaryColor: Color,
    val logoUrl: String,
    val isFavorite: Boolean
)

fun TeamEntity.toDomain() = Team(id, leagueType, city, name,Color(primaryColor), logoUrl, false)

fun TeamWithFavouriteSignEntity.toDomain() =
    Team(team.id, team.leagueType, team.city, team.name, Color(team.primaryColor),team.logoUrl, favouriteTeamEntity.isFavourite)