package com.bashkevich.sportalarmclock.model.settings.domain

import com.bashkevich.sportalarmclock.model.league.LeagueType

data class SettingsData(
    val leagueList: List<LeagueType>,
    val teamsMode: TeamsMode
)
