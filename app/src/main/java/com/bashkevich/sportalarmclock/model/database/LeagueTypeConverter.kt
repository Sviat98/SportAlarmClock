package com.bashkevich.sportalarmclock.model.database

import androidx.room.TypeConverter
import com.bashkevich.sportalarmclock.model.league.LeagueType

class LeagueTypeConverter {

    @TypeConverter
    fun fromLeague(leagueType: LeagueType): String {
        return leagueType.name
    }

    @TypeConverter
    fun toLeague(league: String): LeagueType {
        return LeagueType.valueOf(league)
    }
}