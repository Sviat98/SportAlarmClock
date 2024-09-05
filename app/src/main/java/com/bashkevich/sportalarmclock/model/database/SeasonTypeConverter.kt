package com.bashkevich.sportalarmclock.model.database

import androidx.room.TypeConverter
import com.bashkevich.sportalarmclock.model.season.SeasonType

class SeasonTypeConverter {
    @TypeConverter
    fun fromSeasonType(seasonType: SeasonType): String {
        return seasonType.name
    }

    @TypeConverter
    fun toSeasonType(seasonType: String): SeasonType {
        return SeasonType.valueOf(seasonType)
    }
}