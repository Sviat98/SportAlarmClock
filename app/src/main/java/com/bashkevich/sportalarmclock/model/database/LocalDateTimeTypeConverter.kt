package com.bashkevich.sportalarmclock.model.database

import androidx.room.TypeConverter
import com.bashkevich.sportalarmclock.model.league.League
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant

class LocalDateTimeTypeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.format(LocalDateTime.Formats.ISO)
    }

    @TypeConverter
    fun toLocalDateTime(localDateTime: String): LocalDateTime {
        return LocalDateTime.parse(localDateTime)
    }
}