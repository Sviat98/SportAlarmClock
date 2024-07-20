package com.bashkevich.sportalarmclock.model.team.remote

import com.bashkevich.sportalarmclock.BuildConfig
import com.bashkevich.sportalarmclock.model.network.LoadResult
import com.bashkevich.sportalarmclock.model.network.NetworkUtils
import com.bashkevich.sportalarmclock.model.network.runOperationCatching
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeamRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchNHLTeams(): LoadResult<List<TeamDto>,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val teamsList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NHL_BASE_URL
                    encodedPath = "/AllTeams"
                }
                parameter("key", BuildConfig.NHL_API_KEY)
            }.body<List<TeamDto>>()
            teamsList
        }
    }

    suspend fun fetchMLBTeams(): LoadResult<List<TeamDto>,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val teamsList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.MLB_BASE_URL
                    encodedPath = "/AllTeams"
                }
                parameter("key",BuildConfig.MLB_API_KEY)
            }.body<List<TeamDto>>()
            teamsList
        }
    }

    suspend fun fetchMLBEspnTeams(): LoadResult<List<MLBTeamDto>,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val teamsList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.MLB_ESPN_BASE_URL
                    encodedPath = "/teams"
                }
            }.body<ESPNSportsBody>().sports.first().leagues.first().teams.map { it.team }
            teamsList
        }
    }

    suspend fun fetchNBATeams(): LoadResult<List<TeamDto>,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val teamsList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NBA_BASE_URL
                    encodedPath = "/AllTeams"
                }
                parameter("key",BuildConfig.NBA_API_KEY)
            }.body<List<TeamDto>>()
            teamsList
        }
    }


    suspend fun fetchNFLTeams(): LoadResult<List<NFLTeamDto>,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val teamsList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NFL_BASE_URL
                    encodedPath = "/AllTeams"
                }
                parameter("key",BuildConfig.NFL_API_KEY)
            }.body<List<NFLTeamDto>>()
            teamsList
        }
    }

}