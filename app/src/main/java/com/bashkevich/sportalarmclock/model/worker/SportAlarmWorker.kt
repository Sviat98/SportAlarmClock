package com.bashkevich.sportalarmclock.model.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SportAlarmWorker(
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository,
    appContext: Context,
    private val params: WorkerParameters
) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "SportAlarmWorker"
    }

    override suspend fun doWork(): Result {

        var isSuccess = false

        var nhlTeamsAsync: Deferred<LoadResult<List<Team>, Throwable>>
        var mlbTeamsAsync: Deferred<LoadResult<List<Team>, Throwable>>
        var nbaTeamsAsync: Deferred<LoadResult<List<Team>, Throwable>>
        var nflTeamsAsync: Deferred<LoadResult<List<Team>, Throwable>>

        var mlbMatchesAsync: Deferred<LoadResult<Unit, Throwable>>

        Log.d(WORK_NAME, "START")

        coroutineScope {
            nhlTeamsAsync = async {
                teamRepository.fetchAllNHLTeams()
            }

            Log.d(WORK_NAME, "fetchAllNHLTeams() FINISH")

            mlbTeamsAsync = async {
                teamRepository.fetchAllMLBTeams()
            }

            nbaTeamsAsync = async {
                teamRepository.fetchAllNBATeams()
            }

            nflTeamsAsync = async {
                teamRepository.fetchAllNFLTeams()
            }

            Log.d(WORK_NAME, "fetchAllMLBTeams() FINISH")

        }

        Log.d(WORK_NAME, "FINISH")

        val nhlTeamsResult = nhlTeamsAsync.await()

        val mlbTeamsResult = mlbTeamsAsync.await()

        val nbaTeamsResult = nbaTeamsAsync.await()

        val nflTeamsResult = nflTeamsAsync.await()

        coroutineScope {
            mlbMatchesAsync = async {
                matchRepository.fetchAllMLBMatches("2024")
            }
        }

        val mlbMatchesResult = mlbMatchesAsync.await()


        isSuccess =
            (nhlTeamsResult is LoadResult.Success) &&
                    (mlbMatchesResult is LoadResult.Success) && (nbaTeamsResult is LoadResult.Success) && (nflTeamsResult is LoadResult.Success)

        return if (isSuccess) {
            Result.success()
        } else {
            Result.failure()
        }
    }

}