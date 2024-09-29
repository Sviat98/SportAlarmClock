package com.bashkevich.sportalarmclock.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Matches

@Serializable
data object Teams

@Serializable
data object Settings

@Serializable
data class Alarm(val matchId: Int)