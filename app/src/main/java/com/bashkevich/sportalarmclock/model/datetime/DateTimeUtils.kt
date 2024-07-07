package com.bashkevich.sportalarmclock.model.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

const val AMERICAN_TIME_ZONE = "America/New_York"

fun LocalDateTime.convertFromAmericanTimeZone() =
    this.toInstant(TimeZone.of(AMERICAN_TIME_ZONE)).toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
