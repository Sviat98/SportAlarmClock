package com.bashkevich.sportalarmclock.model.match.domain

import com.bashkevich.sportalarmclock.model.datetime.convertFromAmericanTimeZone
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.local.MatchWithTeamsEntity
import com.bashkevich.sportalarmclock.model.season.SeasonType
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.domain.toDomain
import kotlinx.datetime.LocalDateTime

data class Match(
    val id: Int,
    val leagueType: LeagueType,
    val seasonType: SeasonType,
    val homeTeam: Team,
    val awayTeam: Team,
    val dateTime: LocalDateTime,
    val isChecked: Boolean,
    val isAbleToAlarm: Boolean
)

fun MatchWithTeamsEntity.toDomain() = Match(
    id = matchWithFavouriteSignEntity.matchEntity.id,
    leagueType = matchWithFavouriteSignEntity.matchEntity.leagueType,
    seasonType = matchWithFavouriteSignEntity.matchEntity.seasonType,
    homeTeam = homeTeamEntity.toDomain(),
    awayTeam = awayTeamEntity.toDomain(),
    dateTime = matchWithFavouriteSignEntity.matchEntity.dateTime.convertFromAmericanTimeZone(),
    isChecked = matchWithFavouriteSignEntity.favouriteMatchEntity.isFavourite,
    isAbleToAlarm = true
)



//fun TeamEntity.toDomain() = Team(id, league, city, name, logoUrl, false)
//
//fun TeamWithFavouriteSignEntity.toDomain() =
//    Team(team.id, team.league, team.city, team.name, team.logoUrl, favouriteTeamEntity.isFavourite)