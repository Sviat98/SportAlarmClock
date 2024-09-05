package com.bashkevich.sportalarmclock.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bashkevich.sportalarmclock.model.league.LeagueType
import com.bashkevich.sportalarmclock.model.settings.domain.TeamsMode
import kotlinx.coroutines.flow.map

class SportDatastore(
    private val appContext: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sport_datastore")

    private val stringValueDefault = ""

    private val datastore = appContext.dataStore

    private val PREF_LEAGUES = stringPreferencesKey("leagues")

    private val PREF_TEAMS_MODE = stringPreferencesKey("show_teams")


    suspend fun setTeamsMode(teamsMode: TeamsMode) {
        datastore.edit { preferences ->
            preferences[PREF_TEAMS_MODE] = teamsMode.name
        }
    }

    suspend fun setLeagues(leagueTypes: List<LeagueType>) {
        datastore.edit { preferences ->
            preferences[PREF_LEAGUES] = leagueTypes.joinToString(",") { it.name }
        }
    }

    fun observeTeamsMode() = datastore.data.map { preferences ->
        TeamsMode.valueOf(preferences[PREF_TEAMS_MODE] ?: TeamsMode.ALL.name)
    }

    fun observeLeagues() = datastore.data.map { preferences ->
        preferences[PREF_LEAGUES]?.split(",")?.map { LeagueType.valueOf(it) } ?: listOf(
            LeagueType.NHL,
            LeagueType.NBA,
            LeagueType.MLB,
            LeagueType.NFL
        )
    }
}


