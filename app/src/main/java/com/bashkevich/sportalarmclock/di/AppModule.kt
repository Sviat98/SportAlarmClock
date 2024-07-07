package com.bashkevich.sportalarmclock.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bashkevich.sportalarmclock.model.database.SportDatabase
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepositoryImpl
import com.bashkevich.sportalarmclock.model.match.local.MatchLocalDataSource
import com.bashkevich.sportalarmclock.model.match.remote.MatchRemoteDataSource
import com.bashkevich.sportalarmclock.model.team.local.TeamLocalDataSource
import com.bashkevich.sportalarmclock.model.team.remote.TeamRemoteDataSource
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepositoryImpl
import com.bashkevich.sportalarmclock.model.worker.SportAlarmWorker
import com.bashkevich.sportalarmclock.screens.matches.MatchesViewModel
import com.bashkevich.sportalarmclock.screens.teams.TeamsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.bind

val coreModule = module {

    single {
        HttpClient(Android) {
            expectSuccess = true


            install(HttpTimeout) {
                connectTimeoutMillis = 15000
                requestTimeoutMillis = 30000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true

                    },
                )
            }


            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    single {
        val appContext: Context = get()

        Room.databaseBuilder(
            appContext,
            SportDatabase::class.java,
            "sport_db"
        ).fallbackToDestructiveMigration().build()
    }
    single {
        val sportDatabase: SportDatabase = get()

        sportDatabase.getTeamDao()
    }
    single {
        val sportDatabase: SportDatabase = get()

        sportDatabase.getMatchDao()
    }
}

val workerModule = module {
    // Register the SportAlarmWorker in the module
    workerOf(::SportAlarmWorker)
}

val teamModule = module {
    singleOf(::TeamRemoteDataSource)
    singleOf(::TeamLocalDataSource)
    singleOf(::TeamRepositoryImpl) {
        bind<TeamRepository>()
    }
    viewModelOf(::TeamsViewModel)
}

val matchModule = module {
    singleOf(::MatchRemoteDataSource)
    singleOf(::MatchLocalDataSource)
    singleOf(::MatchRepositoryImpl) {
        bind<MatchRepository>()
    }
    viewModelOf(::MatchesViewModel)
}
