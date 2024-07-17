package com.bashkevich.sportalarmclock.model.team.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.team.remote.NFLTeamDto
import com.bashkevich.sportalarmclock.model.team.remote.TeamDto

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "league")
    val league: League,
    @ColumnInfo(name = "active")
    val isActive: Boolean,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "logo")
    val logoUrl: String,
)

fun TeamDto.toTeamEntity(league: League) = TeamEntity(
    id = id,
    league = league,
    isActive = isActive,
    city = city,
    name = name,
    logoUrl = if (league == League.MLB) wordMarkUrl ?: logoUrl ?: "" else logoUrl ?: ""
)

fun NFLTeamDto.toTeamEntity() = TeamEntity(
    id = id,
    league = League.NFL,
    isActive = stadiumId!=null,
    city = city,
    name = name,
    logoUrl = logoUrl ?: ""
)
