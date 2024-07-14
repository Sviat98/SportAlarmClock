package com.bashkevich.sportalarmclock.model.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.season.remote.SeasonDto
import com.bashkevich.sportalarmclock.model.team.domain.Team
import com.bashkevich.sportalarmclock.model.season.repository.SeasonRepository
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SportAlarmWorker(
    private val seasonRepository: SeasonRepository,
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

        var nhlSeasonAsync: Deferred<LoadResult<SeasonDto, Throwable>>
        var mlbSeasonAsync: Deferred<LoadResult<SeasonDto, Throwable>>
        var nbaSeasonAsync: Deferred<LoadResult<SeasonDto, Throwable>>
        var nflSeasonAsync: Deferred<LoadResult<SeasonDto, Throwable>>

        var nhlMatchesAsync: Deferred<LoadResult<Unit, Throwable>>? = null
        var mlbMatchesAsync: Deferred<LoadResult<Unit, Throwable>>? = null
        var nbaMatchesAsync: Deferred<LoadResult<Unit, Throwable>>? = null
        var nflMatchesAsync: Deferred<LoadResult<Unit, Throwable>>? = null

        Log.d(WORK_NAME, "START")

        coroutineScope {
            nhlSeasonAsync = async {
                seasonRepository.fetchNHLCurrentSeason()
            }

            Log.d(WORK_NAME, "fetchAllNHLTeams() FINISH")

            mlbSeasonAsync = async {
                seasonRepository.fetchMLBCurrentSeason()
            }

            nbaSeasonAsync = async {
                seasonRepository.fetchNBACurrentSeason()
            }

            nflSeasonAsync = async {
                seasonRepository.fetchNFLCurrentSeason()
            }

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

        val nhlSeasonResult = nhlSeasonAsync.await()

        Log.d(WORK_NAME, "nhlSeasonResult $nhlSeasonResult")


        val mlbSeasonResult = mlbSeasonAsync.await()

        Log.d(WORK_NAME, "mlbSeasonResult $mlbSeasonResult")


        val nbaSeasonResult = nbaSeasonAsync.await()

        Log.d(WORK_NAME, "nbaSeasonResult $nbaSeasonResult")


        val nflSeasonResult = nflSeasonAsync.await()

        Log.d(WORK_NAME, "nflSeasonResult $nflSeasonResult")


        coroutineScope {
            if (nhlSeasonResult is LoadResult.Success) {
                nhlMatchesAsync = async {
                    matchRepository.fetchAllNHLMatches(nhlSeasonResult.result.apiSeason)
                    //matchRepository.fetchAllNHLMatches("2024")
                }
            }

            if (mlbSeasonResult is LoadResult.Success) {
                mlbMatchesAsync = async {
                    matchRepository.fetchAllMLBMatches(mlbSeasonResult.result.apiSeason)
                }
            }

            if (nbaSeasonResult is LoadResult.Success) {
                nbaMatchesAsync = async {
                    matchRepository.fetchAllNBAMatches(nbaSeasonResult.result.apiSeason)
                    //matchRepository.fetchAllNBAMatches("2024")
                }
            }

            if (nflSeasonResult is LoadResult.Success) {
                nflMatchesAsync = async {
                    matchRepository.fetchAllNFLMatches(nflSeasonResult.result.apiSeason)
                    //matchRepository.fetchAllNFLMatches("2024")
                }
            }
        }

        val nhlMatchesResult = nhlMatchesAsync?.await()

        Log.d(WORK_NAME, "nhlMatchesResult $nhlMatchesResult")


        val mlbMatchesResult = mlbMatchesAsync?.await()

        Log.d(WORK_NAME, "mlbMatchesResult $mlbMatchesResult")


        val nbaMatchesResult = nbaMatchesAsync?.await()

        Log.d(WORK_NAME, "nbaMatchesResult $nbaMatchesResult")


        val nflMatchesResult = nflMatchesAsync?.await()

        Log.d(WORK_NAME, "nflMatchesResult $nflMatchesResult")

        Log.d(WORK_NAME,"HERE WE ARE")

        isSuccess =
            (nhlMatchesResult is LoadResult.Success) &&
                    (mlbMatchesResult is LoadResult.Success) && (nbaMatchesResult is LoadResult.Success) && (nflMatchesResult is LoadResult.Success)

        return if (isSuccess) {
            Result.success()
        } else {
            Result.failure()
        }
    }

}