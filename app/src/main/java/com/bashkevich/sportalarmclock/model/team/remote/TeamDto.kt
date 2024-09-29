package com.bashkevich.sportalarmclock.model.team.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    @SerialName(value = "GlobalTeamID")
    val id: Int,
    @SerialName(value = "Active")
    val isActive: Boolean,
    @SerialName(value = "City")
    val city: String,
    @SerialName(value = "Name")
    val name: String,
    @SerialName(value = "PrimaryColor")
    val primaryColor: String?,
    @SerialName(value = "SecondaryColor")
    val secondaryColor: String?,
    @SerialName(value = "WikipediaLogoUrl")
    val logoUrl: String?,
    @SerialName(value = "WikipediaWordMarkUrl")
    val wordMarkUrl: String?
)