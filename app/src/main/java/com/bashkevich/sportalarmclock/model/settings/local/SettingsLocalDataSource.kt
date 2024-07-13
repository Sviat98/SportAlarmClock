package com.bashkevich.sportalarmclock.model.settings.local

import com.bashkevich.sportalarmclock.model.datastore.SportDatastore
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow

class SettingsLocalDataSource(
    private val datastore: SportDatastore
) {
    suspend fun setLeaguesList(leagues: List<League>) {
        datastore.setLeagues(leagues)
    }

    fun observeLeaguesList(): Flow<List<League>> = datastore.observeLeagues()

    suspend fun setTeamsMode(teamsMode: TeamsMode) {
        datastore.setTeamsMode(teamsMode)
    }

    fun observeTeamsMode(): Flow<TeamsMode> = datastore.observeTeamsMode()
}