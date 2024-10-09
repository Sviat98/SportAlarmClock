package com.bashkevich.sportalarmclock.model.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

const val EASTERN_AMERICA_TIME_ZONE = "America/New_York"

const val WESTERN_AMERICA_TIME_ZONE = "America/Los_Angeles"

fun LocalDateTime.convertFromEasternAmericaTimeZone() =
    this.toInstant(TimeZone.of(EASTERN_AMERICA_TIME_ZONE)).toLocalDateTime(
        TimeZone.currentSystemDefault()
    )

fun LocalDateTime.convertToEasternAmericaTimeZone() =
    this.toInstant(TimeZone.currentSystemDefault()).toLocalDateTime(
        TimeZone.of(EASTERN_AMERICA_TIME_ZONE)
    )
