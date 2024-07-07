package com.bashkevich.sportalarmclock.model.match.domain

import com.bashkevich.sportalarmclock.model.datetime.convertFromAmericanTimeZone
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.local.MatchEntity
import com.bashkevich.sportalarmclock.model.match.local.MatchWithTeamsEntity
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.domain.toDomain
import kotlinx.datetime.LocalDateTime

data class Match(
    val id: Int,
    val league: League,
    val homeTeam: Team,
    val awayTeam: Team,
    val dateTime: LocalDateTime,
    val isChecked: Boolean
)

fun MatchWithTeamsEntity.toDomain() = Match(
    id = matchEntity.id,
    league = matchEntity.league,
    homeTeam = homeTeamEntity.toDomain(),
    awayTeam = awayTeamEntity.toDomain(),
    dateTime = matchEntity.dateTime.convertFromAmericanTimeZone(),
    isChecked = false
)



//fun TeamEntity.toDomain() = Team(id, league, city, name, logoUrl, false)
//
//fun TeamWithFavouriteSignEntity.toDomain() =
//    Team(team.id, team.league, team.city, team.name, team.logoUrl, favouriteTeamEntity.isFavourite)