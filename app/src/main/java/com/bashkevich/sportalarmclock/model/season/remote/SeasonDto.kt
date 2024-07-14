package com.bashkevich.sportalarmclock.model.season.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDto(
    @SerialName(value = "Season")
    val season: Int,
    @SerialName(value = "SeasonType")
    val seasonType: String,
    @SerialName(value = "ApiSeason")
    val apiSeason: String
)