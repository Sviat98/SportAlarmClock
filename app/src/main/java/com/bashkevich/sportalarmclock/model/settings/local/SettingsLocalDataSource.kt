package com.bashkevich.sportalarmclock.model.settings.local

import com.bashkevich.sportalarmclock.model.datastore.SportDatastore
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.SettingsData
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SettingsLocalDataSource(
    private val datastore: SportDatastore
) {
    suspend fun setLeaguesList(leagueTypes: List<LeagueType>) {
        datastore.setLeagues(leagueTypes)
    }

    fun observeSettingsData(): Flow<SettingsData> =
        combine(
            datastore.observeLeagues(),
            datastore.observeTeamsMode()
        ) { leagues: List<LeagueType>, teamsMode: TeamsMode ->
            SettingsData(leagues, teamsMode)
        }

    suspend fun setTeamsMode(teamsMode: TeamsMode) {
        datastore.setTeamsMode(teamsMode)
    }

    fun observeTeamsMode(): Flow<TeamsMode> = datastore.observeTeamsMode()
}