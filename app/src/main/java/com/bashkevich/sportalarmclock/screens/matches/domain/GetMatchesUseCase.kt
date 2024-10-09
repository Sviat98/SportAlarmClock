package com.bashkevich.sportalarmclock.screens.matches.domain

import android.util.Log
import com.bashkevich.sportalarmclock.model.datetime.EASTERN_AMERICA_TIME_ZONE
import com.bashkevich.sportalarmclock.model.datetime.convertToEasternAmericaTimeZone
import com.bashkevich.sportalarmclock.model.datetime.repository.DateTimeRepository
import com.bashkevich.sportalarmclock.model.match.domain.Match
import com.bashkevich.sportalarmclock.model.match.repository.MatchRepository
import com.bashkevich.sportalarmclock.model.quadruple.Quadruple
import com.bashkevich.sportalarmclock.model.settings.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetMatchesUseCase(
    private val dateTimeRepository: DateTimeRepository,
    private val settingsRepository: SettingsRepository,
    private val matchRepository: MatchRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<List<Match>> {
        val currentSystemDateTimeMinusHourFlow =
            dateTimeRepository.observeCurrentTimeZone().distinctUntilChanged()
                .flatMapLatest { timeZone ->
                    dateTimeRepository.observeCurrentDateTimeMinusHour(timeZone = timeZone)
                        .distinctUntilChanged()
                }
        return combine(
            dateTimeRepository.observeCurrentTimeZone().distinctUntilChanged(),
            dateTimeRepository.observeSelectedDate(),
            currentSystemDateTimeMinusHourFlow,
            settingsRepository.observeSettingsData().distinctUntilChanged(),
        ) { timeZone, selectedDate, currentDateMinusHour, settingsData ->
            Quadruple(timeZone, selectedDate, currentDateMinusHour, settingsData)
        }.flatMapLatest { (timeZone, selectedDate, currentDateMinusHour, settingsData) ->

            val teamsMode = settingsData.teamsMode

            val leaguesList = settingsData.leagueList

            Log.d(
                "MatchesViewModel observe matches",
                "$timeZone $selectedDate $teamsMode $leaguesList"
            )

            val dateAtLocalMidnight = selectedDate.atStartOfDayIn(timeZone).toLocalDateTime(
                TimeZone.of(EASTERN_AMERICA_TIME_ZONE)
            )

            val dateTimeMinusHourAmerica = currentDateMinusHour.convertToEasternAmericaTimeZone()

            val dateTimeBegin = dateTimeMinusHourAmerica.coerceAtLeast(dateAtLocalMidnight)

            Log.d(
                "MatchesViewModel dateTimeBegin",
                "$dateTimeBegin"
            )

            val dateTimeEnd = selectedDate.atStartOfDayIn(timeZone).plus(
                DateTimePeriod(hours = 23, minutes = 59, seconds = 59), TimeZone.of(
                    EASTERN_AMERICA_TIME_ZONE
                )
            ).toLocalDateTime(TimeZone.of(EASTERN_AMERICA_TIME_ZONE))

            matchRepository.observeMatchesByDate(dateTimeBegin, dateTimeEnd, leaguesList, teamsMode)
                .distinctUntilChanged()
        }.map { matches ->
            val matchesByDate = matches.groupBy { it.dateTime }.filter {
                it.value.any { match -> match.isChecked }
            }

            val mutableMatches = matches.toMutableList()

            Log.d("MatchesViewModel matchesByDate", matchesByDate.toString())

            matchesByDate.forEach { (dateTime, matchesOnDate) ->
                val matchesToDisable = matchesOnDate.filter { !it.isChecked }

                matchesToDisable.forEach { matchToDisable ->
                    val index = mutableMatches.indexOf(matchToDisable)

                    Log.d("MatchesViewModel matchToDisable", "$matchToDisable $index")


                    mutableMatches[index] = matchToDisable.copy(isAbleToAlarm = false)

                    Log.d(
                        "MatchesViewModel match",
                        matchToDisable.copy(isAbleToAlarm = false).toString()
                    )
                }
            }

            Log.d("MatchesViewModel matches", mutableMatches.toString())

            mutableMatches.toList()
        }
    }
}