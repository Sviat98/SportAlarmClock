package com.bashkevich.sportalarmclock.model.match.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.match.remote.MatchDto
import com.bashkevich.sportalarmclock.model.match.remote.NFLMatchDto
import com.bashkevich.sportalarmclock.model.season.SeasonType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "match")
data class MatchEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "league")
    val leagueType: LeagueType,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "season_type")
    val seasonType: SeasonType,
    @ColumnInfo(name = "home_team_id")
    val homeTeamId: Int,
    @ColumnInfo(name = "away_team_id")
    val awayTeamId: Int,
    @ColumnInfo(name = "date_time")
    val dateTime: LocalDateTime
)

fun MatchDto.toMatchEntity(leagueType: LeagueType, season: Int, seasonType: SeasonType) = MatchEntity(
    id = id.countGlobalId(leagueType),
    leagueType = leagueType,
    season = season,
    seasonType = seasonType,
    homeTeamId = homeTeamId.countGlobalId(leagueType),
    awayTeamId = awayTeamId.countGlobalId(leagueType),
    dateTime = dateTime ?: LocalDateTime(1970, 1, 1, 0, 0, 0)
)

fun NFLMatchDto.toMatchEntity(leagueType: LeagueType, season: Int, seasonType: SeasonType) = MatchEntity(
    id = id?.countGlobalId(leagueType) ?: 0,
    leagueType = leagueType,
    season = season,
    seasonType = seasonType,
    homeTeamId = homeTeamId?.countGlobalId(leagueType) ?: 0,
    awayTeamId = awayTeamId?.countGlobalId(leagueType) ?: 0,
    dateTime = dateTime ?: LocalDateTime(1970, 1, 1, 0, 0, 0)
)

private fun Int.countGlobalId(leagueType: LeagueType) =
    when (leagueType) {
        LeagueType.NHL -> this + 30000000
        LeagueType.MLB -> this + 10000000
        LeagueType.NBA -> this + 20000000
        LeagueType.NFL -> this
    }
