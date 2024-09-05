package com.bashkevich.sportalarmclock.model.settings.local

import com.bashkevich.sportalarmclock.model.datastore.SportDatastore
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow

class SettingsLocalDataSource(
    private val datastore: SportDatastore
) {
    suspend fun setLeaguesList(leagueTypes: List<LeagueType>) {
        datastore.setLeagues(leagueTypes)
    }

    fun observeLeaguesList(): Flow<List<LeagueType>> = datastore.observeLeagues()

    suspend fun setTeamsMode(teamsMode: TeamsMode) {
        datastore.setTeamsMode(teamsMode)
    }

    fun observeTeamsMode(): Flow<TeamsMode> = datastore.observeTeamsMode()
}