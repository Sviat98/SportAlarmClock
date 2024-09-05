package com.bashkevich.sportalarmclock.model.settings.repository

import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setLeaguesList(leagueTypes: List<LeagueType>)
    fun observeLeaguesList(): Flow<List<LeagueType>>
    suspend fun setTeamsMode(teamsMode: TeamsMode)
    fun observeTeamsMode(): Flow<TeamsMode>
}