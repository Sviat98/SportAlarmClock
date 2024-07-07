package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.match.remote.MatchDto
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity
import com.bashkevich.sportalarmclock.model.team.remote.TeamDto
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "match")
data class MatchEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "league")
    val league: League,
    @ColumnInfo(name = "home_team_id")
    val homeTeamId: Int,
    @ColumnInfo(name = "away_team_id")
    val awayTeamId: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime
)

fun MatchDto.toMatchEntity(league: League) = MatchEntity(
    id = id.countGlobalId(league),
    league = league,
    homeTeamId = homeTeamId.countGlobalId(league),
    awayTeamId = awayTeamId.countGlobalId(league),
    dateTime = dateTime!!
)

private fun Int.countGlobalId(league: League) =
    when (league) {
        League.NHL -> this + 30000000
        League.MLB -> this + 10000000
        League.NBA -> this + 20000000
        League.NFL -> this
    }
