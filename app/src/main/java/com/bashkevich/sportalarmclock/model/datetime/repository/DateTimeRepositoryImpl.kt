package com.bashkevich.sportalarmclock.model.datetime.repository

import com.bashkevich.sportalarmclock.model.datetime.local.DateTimeLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class DateTimeRepositoryImpl(
    private val dateTimeLocalDataSource: DateTimeLocalDataSource
) : DateTimeRepository {
    private val _selectedDate = MutableStateFlow(LocalDate(1970, 1, 1))

    override fun observeCurrentTimeZone(): Flow<TimeZone> =
        dateTimeLocalDataSource.observeCurrentTimeZone()

    override fun observeIs24HourFormat(): Flow<Boolean> =
        dateTimeLocalDataSource.observeTimeFormat()

    override fun observeSelectedDate(): Flow<LocalDate> = _selectedDate.asStateFlow()

    override fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    override fun observeCurrentSystemDate(timeZone: TimeZone) = dateTimeLocalDataSource.observeCurrentSystemDate(timeZone = timeZone)
}