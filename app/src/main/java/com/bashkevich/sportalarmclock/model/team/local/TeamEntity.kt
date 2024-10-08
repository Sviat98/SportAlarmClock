package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.team.remote.MLBTeamDto
import com.bashkevich.sportalarmclock.model.team.remote.NFLTeamDto
import com.bashkevich.sportalarmclock.model.team.remote.TeamDto

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "league")
    val leagueType: LeagueType,
    @ColumnInfo(name = "active")
    val isActive: Boolean,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "primary_color")
    val primaryColor: Long,
    @ColumnInfo(name = "secondary_color")
    val secondaryColor: Long,
    @ColumnInfo(name = "logo")
    val logoUrl: String,
)

fun TeamDto.toTeamEntity(leagueType: LeagueType) = TeamEntity(
    id = id,
    leagueType = leagueType,
    isActive = isActive,
    city = city,
    name = name,
    primaryColor = primaryColor.convertColor(),
    secondaryColor = secondaryColor.convertColor(),
    logoUrl = if (leagueType == LeagueType.MLB) wordMarkUrl ?: logoUrl ?: "" else logoUrl ?: ""
)

fun NFLTeamDto.toTeamEntity() = TeamEntity(
    id = id,
    leagueType = LeagueType.NFL,
    isActive = stadiumId != null,
    city = city,
    name = name,
    primaryColor = primaryColor.convertColor(),
    secondaryColor = secondaryColor.convertColor(),
    logoUrl = logoUrl ?: ""
)

fun MLBTeamDto.toTeamEntity() = TeamEntity(
    id = id.toInt(),
    leagueType = LeagueType.MLB,
    isActive = true,
    city = city,
    name = name,
    primaryColor = 0L,
    secondaryColor = 0L,
    logoUrl = logos[0].imageUrl
)

private fun String?.convertColor() = if (this == null) "FFFFFFFF".toLong(16)
else "FF$this".toLong(16)