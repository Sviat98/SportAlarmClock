package com.bashkevich.sportalarmclock.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bashkevich.sportalarmclock.model.match.local.FavouriteMatchEntity
import com.bashkevich.sportalarmclock.model.match.local.MatchDao
import com.bashkevich.sportalarmclock.model.match.local.MatchEntity
import com.bashkevich.sportalarmclock.model.team.local.FavouriteTeamEntity
import com.bashkevich.sportalarmclock.model.team.local.TeamDao
import com.bashkevich.sportalarmclock.model.team.local.TeamEntity

@Database(entities = [TeamEntity::class, FavouriteTeamEntity::class, MatchEntity::class,FavouriteMatchEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [LeagueTypeConverter::class,LocalDateTimeTypeConverter::class,SeasonTypeConverter::class])
abstract class SportDatabase : RoomDatabase() {
    abstract fun getTeamDao() : TeamDao
    abstract fun getMatchDao() : MatchDao
}