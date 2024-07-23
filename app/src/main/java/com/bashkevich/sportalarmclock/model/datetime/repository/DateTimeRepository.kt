package com.bashkevich.sportalarmclock.model.datetime.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

interface DateTimeRepository {
    fun observeCurrentTimeZone(): Flow<TimeZone>
    fun observeSelectedDate(): Flow<LocalDate>
    fun selectDate(date: LocalDate)
    fun observeCurrentSystemDate(timeZone: TimeZone): Flow<LocalDate>
}