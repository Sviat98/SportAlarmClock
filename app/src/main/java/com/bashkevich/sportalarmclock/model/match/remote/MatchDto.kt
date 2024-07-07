package com.bashkevich.sportalarmclock.model.match.remote

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchDto(
    @SerialName(value = "GameID")
    val id: Int,
    @SerialName(value = "HomeTeamID")
    val homeTeamId: Int,
    @SerialName(value = "AwayTeamID")
    val awayTeamId: Int,
    @SerialName(value = "DateTime")
    val dateTime: LocalDateTime? = null,
    @SerialName(value = "Status")
    val status: String
)