package com.bashkevich.sportalarmclock.model.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

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