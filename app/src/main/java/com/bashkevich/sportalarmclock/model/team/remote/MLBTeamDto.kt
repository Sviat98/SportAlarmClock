package com.bashkevich.sportalarmclock.model.team.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// MLB Team Dto for logos on transparent background from ESPN API
//https://site.api.espn.com/apis/site/v2/sports/baseball/mlb/teams

@Serializable
data class MLBTeamDto(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "location")
    val city: String,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "logos")
    val logos: List<MLBTeamLogo>
)
@Serializable
data class MLBTeamLogo(
    @SerialName(value = "href")
    val imageUrl: String
)