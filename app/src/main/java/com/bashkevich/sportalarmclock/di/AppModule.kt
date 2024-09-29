package com.bashkevich.sportalarmclock.di

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.bashkevich.sportalarmclock.alarm.AlarmScheduler
import com.bashkevich.sportalarmclock.alarm.AlarmSchedulerImpl
import com.bashkevich.sportalarmclock.model.database.SportDatabase
import com.bashkevich.sportalarmclock.model.datastore.SportDatastore
import com.bashkevich.sportalarmclock.model.datetime.local.DateTimeLocalDataSource
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepository
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepositoryImpl
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepositoryImpl
import com.bashkevich.sportalarmclock.model.match.local.MatchLocalDataSource
import com.bashkevich.sportalarmclock.model.match.remote.MatchRemoteDataSource
import com.bashkevich.sportalarmclock.model.settings.local.SettingsLocalDataSource
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepository
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepositoryImpl
import com.bashkevich.sportalarmclock.model.team.local.TeamLocalDataSource
import com.bashkevich.sportalarmclock.model.season.remote.SeasonRemoteDataSource
import com.bashkevich.sportalarmclock.model.season.repository.SeasonRepository
import com.bashkevich.sportalarmclock.model.season.repository.SeasonRepositoryImpl
import com.bashkevich.sportalarmclock.model.team.remote.TeamRemoteDataSource
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepository
import com.bashkevich.sportalarmclock.model.team.repository.TeamRepositoryImpl
import com.bashkevich.sportalarmclock.model.worker.SportAlarmWorker
import com.bashkevich.sportalarmclock.screens.alarm.AlarmViewModel
import com.bashkevich.sportalarmclock.screens.matches.MatchesViewModel
import com.bashkevich.sportalarmclock.screens.settings.SettingsViewModel
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
    single {
        val appContext: Context = get()

        SportDatastore(appContext)
    }

    single {
        val appContext: Context = get()

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager
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

val alarmModule = module {
    singleOf(::AlarmSchedulerImpl) {
        bind<AlarmScheduler>()
    }
    viewModelOf(::AlarmViewModel)
}

val matchModule = module {
    singleOf(::MatchRemoteDataSource)
    singleOf(::MatchLocalDataSource)
    singleOf(::MatchRepositoryImpl) {
        bind<MatchRepository>()
    }
    viewModelOf(::MatchesViewModel)
}

val seasonModule = module {
    singleOf(::SeasonRemoteDataSource)
    singleOf(::SeasonRepositoryImpl) {
        bind<SeasonRepository>()
    }
}

val settingsModule = module {
    singleOf(::SettingsLocalDataSource)
    singleOf(::SettingsRepositoryImpl) {
        bind<SettingsRepository>()
    }
    viewModelOf(::SettingsViewModel)
}

val dateTimeModule = module {
    singleOf(::DateTimeLocalDataSource)
    singleOf(::DateTimeRepositoryImpl) {
        bind<DateTimeRepository>()
    }
}
