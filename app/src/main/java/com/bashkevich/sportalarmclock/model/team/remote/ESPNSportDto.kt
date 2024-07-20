package com.bashkevich.sportalarmclock.model.team.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ESPNSportsBody(
    @SerialName(value = "sports")
    val sports: List<ESPNSportDto>
)

@Serializable
data class ESPNSportDto(
    @SerialName(value = "leagues")
    val leagues: List<ESPNLeague>
)

@Serializable
data class ESPNLeague(
    @SerialName(value = "teams")
    val teams: List<MLBTeamParentDto>
)

@Serializable
data class MLBTeamParentDto(
    @SerialName(value = "team")
    val team: MLBTeamDto
)