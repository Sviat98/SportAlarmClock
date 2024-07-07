package com.bashkevich.sportalarmclock.model.database

import androidx.room.TypeConverter
import com.bashkevich.sportalarmclock.model.league.League

class LeagueTypeConverter {

    @TypeConverter
    fun fromLeague(league: League): String {
        return league.name
    }

    @TypeConverter
    fun toLeague(league: String): League {
        return League.valueOf(league)
    }
}