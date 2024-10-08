package com.bashkevich.sportalarmclock.model.settings.repository

import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.SettingsData
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.model.settings.local.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {
    override suspend fun setLeaguesList(leagueTypes: List<LeagueType>) {
        settingsLocalDataSource.setLeaguesList(leagueTypes)
    }

    override fun observeSettingsData(): Flow<SettingsData> =
        settingsLocalDataSource.observeSettingsData()

    override suspend fun setTeamsMode(teamsMode: TeamsMode) {
        settingsLocalDataSource.setTeamsMode(teamsMode)
    }
}