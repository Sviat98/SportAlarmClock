package com.bashkevich.sportalarmclock.model.settings.repository

import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import com.bashkevich.sportalarmclock.model.settings.local.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsLocalDataSource: SettingsLocalDataSource
) : SettingsRepository {
    override suspend fun setLeaguesList(leagues: List<League>) {
        settingsLocalDataSource.setLeaguesList(leagues)
    }

    override fun observeLeaguesList(): Flow<List<League>> =
        settingsLocalDataSource.observeLeaguesList()

    override suspend fun setTeamsMode(teamsMode: TeamsMode) {
        settingsLocalDataSource.setTeamsMode(teamsMode)
    }

    override fun observeTeamsMode(): Flow<TeamsMode> = settingsLocalDataSource.observeTeamsMode()
}