package com.bashkevich.sportalarmclock

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bashkevich.sportalarmclock.di.coreModule
import com.bashkevich.sportalarmclock.di.dateTimeModule
import com.bashkevich.sportalarmclock.di.matchModule
import com.bashkevich.sportalarmclock.di.seasonModule
import com.bashkevich.sportalarmclock.di.settingsModule
import com.bashkevich.sportalarmclock.di.teamModule
import com.bashkevich.sportalarmclock.di.workerModule
import com.bashkevich.sportalarmclock.model.worker.SportAlarmWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class SportAlarmClockApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(coreModule + teamModule + matchModule + seasonModule + settingsModule + workerModule + dateTimeModule)
            workManagerFactory()
        }

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SportAlarmWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequest.Builder(SportAlarmWorker::class.java, 6L, TimeUnit.HOURS).build()
        )
    }
}