package com.bashkevich.sportalarmclock.model.team.local

import com.bashkevich.sportalarmclock.model.database.SportDatabase
import com.bashkevich.sportalarmclock.model.league.League
import com.bashkevich.sportalarmclock.model.team.domain.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TeamLocalDataSource(
    private val teamDao: TeamDao
) {
    suspend fun replaceTeamsList(teams: List<TeamEntity>) = withContext(Dispatchers.IO) {
        teamDao.replaceTeamsList(teams)
    }

    fun observeTeamsByLeague(league: League): Flow<List<TeamWithFavouriteSignEntity>> =
        teamDao.getAllLeagueTeams(league = league)


    suspend fun toggleFavouriteSign(teamId: Int, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            teamDao.updateFavTeamSign(teamId = teamId, isFavourite = isFavourite)
        }


}