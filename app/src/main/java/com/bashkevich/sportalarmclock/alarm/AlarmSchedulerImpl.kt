package com.bashkevich.sportalarmclock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bashkevich.sportalarmclock.model.datetime.AMERICAN_TIME_ZONE
import com.bashkevich.sportalarmclock.model.datetime.convertFromAmericanTimeZone
import com.bashkevich.sportalarmclock.model.match.domain.Match
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class AlarmSchedulerImpl(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    override fun schedule(matchId: Int, dateTime: LocalDateTime) {
        val intent = Intent(context, AlarmReceiver::class.java)

        intent.putExtra("MATCH_ID",matchId)

        Log.d("AlarmSchedulerImpl schedule match","$matchId $dateTime")
        val alarmTime = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                matchId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(matchId: Int) {
        alarmManager?.cancel(
            PendingIntent.getBroadcast(
                context,
                matchId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}