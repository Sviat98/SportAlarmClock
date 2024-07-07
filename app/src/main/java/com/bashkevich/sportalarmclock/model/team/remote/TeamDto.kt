package com.bashkevich.sportalarmclock.model.team.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    @SerialName(value = "GlobalTeamID")
    val id: Int,
    @SerialName(value = "City")
    val city: String,
    @SerialName(value = "Name")
    val name: String,
    @SerialName(value = "WikipediaLogoUrl")
    val logoUrl: String,
    @SerialName(value = "WikipediaWordMarkUrl")
    val wordMarkUrl: String?
)