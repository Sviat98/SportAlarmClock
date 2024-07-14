package com.bashkevich.sportalarmclock.model.season.remote

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

class SeasonRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun fetchNHLCurrentSeason(): LoadResult<SeasonDto,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val currentSeason = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NHL_BASE_URL
                    encodedPath = "/CurrentSeason"
                }
                parameter("key", BuildConfig.NHL_API_KEY)
            }.body<SeasonDto>()
            currentSeason
        }
    }

    suspend fun fetchMLBCurrentSeason(): LoadResult<SeasonDto,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val currentSeason = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.MLB_BASE_URL
                    encodedPath = "/CurrentSeason"
                }
                parameter("key",BuildConfig.MLB_API_KEY)
            }.body<SeasonDto>()
            currentSeason
        }
    }

    suspend fun fetchNBACurrentSeason(): LoadResult<SeasonDto,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val currentSeason = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NBA_BASE_URL
                    encodedPath = "/CurrentSeason"
                }
                parameter("key",BuildConfig.NBA_API_KEY)
            }.body<SeasonDto>()
            currentSeason
        }
    }


    suspend fun fetchNFLCurrentSeason(): LoadResult<SeasonDto,Throwable> = withContext(Dispatchers.IO){
        runOperationCatching {
            val currentSeasonList = httpClient.get{
                contentType(ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    host = NetworkUtils.NFL_BASE_URL
                    encodedPath = "/Timeframes/current"
                }
                parameter("key",BuildConfig.NFL_API_KEY)
            }.body<List<SeasonDto>>()
            currentSeasonList.first()
        }
    }

}