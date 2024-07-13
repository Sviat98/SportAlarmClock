package com.bashkevich.sportalarmclock.model.settings.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setLeaguesList(leagues: List<League>)
    fun observeLeaguesList(): Flow<List<League>>
    suspend fun setTeamsMode(teamsMode: TeamsMode)
    fun observeTeamsMode(): Flow<TeamsMode>
}