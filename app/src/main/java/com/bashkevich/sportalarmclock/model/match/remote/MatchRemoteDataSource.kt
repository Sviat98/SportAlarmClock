package com.bashkevich.sportalarmclock.model.match.remote

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

class MatchRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchNHLMatches(season: String): LoadResult<List<MatchDto>, Throwable> =
        withContext(Dispatchers.IO) {
            runOperationCatching {
                val matchList = httpClient.get {
                    contentType(ContentType.Application.Json)
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NetworkUtils.NHL_BASE_URL
                        encodedPath = "/SchedulesBasic/$season"
                    }
                    parameter("key", BuildConfig.NHL_API_KEY)
                }.body<List<MatchDto>>()
                matchList
            }
        }

    suspend fun fetchMLBMatches(season: String): LoadResult<List<MatchDto>, Throwable> =
        withContext(Dispatchers.IO) {
            runOperationCatching {
                val matchList = httpClient.get {
                    contentType(ContentType.Application.Json)
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NetworkUtils.MLB_BASE_URL
                        encodedPath = "/SchedulesBasic/$season"
                    }
                    parameter("key", BuildConfig.MLB_API_KEY)
                }.body<List<MatchDto>>()
                matchList
            }
        }

    suspend fun fetchNBAMatches(season: String): LoadResult<List<MatchDto>, Throwable> =
        withContext(Dispatchers.IO) {
            runOperationCatching {
                val matchList = httpClient.get {
                    contentType(ContentType.Application.Json)
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NetworkUtils.NBA_BASE_URL
                        encodedPath = "/SchedulesBasic/$season"
                    }
                    parameter("key", BuildConfig.NBA_API_KEY)
                }.body<List<MatchDto>>()
                matchList
            }
        }

    suspend fun fetchNFLMatches(season: String): LoadResult<List<NFLMatchDto>, Throwable> =
        withContext(Dispatchers.IO) {
            runOperationCatching {
                val matchList = httpClient.get {
                    contentType(ContentType.Application.Json)
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NetworkUtils.NFL_BASE_URL
                        encodedPath = "/SchedulesBasic/$season"
                    }
                    parameter("key", BuildConfig.NFL_API_KEY)
                }.body<List<NFLMatchDto>>()
                matchList
            }
        }


}