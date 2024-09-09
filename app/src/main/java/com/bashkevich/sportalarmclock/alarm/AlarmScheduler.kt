package com.bashkevich.sportalarmclock.alarm

import kotlinx.datetime.LocalDateTime

interface AlarmScheduler {
    fun schedule(matchId: Int, dateTime: LocalDateTime)
    fun cancel(matchId: Int)
}