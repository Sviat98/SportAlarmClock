package com.bashkevich.sportalarmclock.screens.matches.domain

import android.util.Log
import com.bashkevich.sportalarmclock.model.datetime.WESTERN_AMERICA_TIME_ZONE
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetDatesUseCase(
    private val dateTimeRepository: DateTimeRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<List<Pair<LocalDate, Boolean>>> {
        val currentSystemDateFlow = dateTimeRepository.observeCurrentTimeZone()
            .flatMapLatest { timeZone ->
                dateTimeRepository.observeCurrentSystemDate(timeZone = timeZone)
                    .distinctUntilChanged()
            }

        return combine(
            dateTimeRepository.observeCurrentTimeZone(),
            dateTimeRepository.observeCurrentPacificSystemDate().distinctUntilChanged(),
            currentSystemDateFlow
        ) { timeZone, currentPacificDate, currentSystemDate ->
            Triple(timeZone, currentPacificDate, currentSystemDate)
        }.map { (timeZone, currentPacificDate, currentSystemDate) ->
            val startDate =
                currentPacificDate.atStartOfDayIn(TimeZone.of(WESTERN_AMERICA_TIME_ZONE))
                    .toLocalDateTime(timeZone).date
            // переводим дату полуночи в LA на наше время и выделяем дату

            val dates = (0..7).map {
                startDate.plus(it, DateTimeUnit.DAY)
            }

            dates.map { Pair(it, it == currentSystemDate) }
        }.onEach {dates->
            val selectedDate = dateTimeRepository.observeSelectedDate().first()
            // при запуске приложения дата 01.01.1970, что вызывывает функцию selectDate

            val today = dates.first { it.second }.first
            if (selectedDate < today) {
                dateTimeRepository.selectDate(today)
            }
        }
    }

}