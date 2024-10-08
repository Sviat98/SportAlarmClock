package com.bashkevich.sportalarmclock.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.bashkevich.sportalarmclock.AlarmActivity
import com.bashkevich.sportalarmclock.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra(MATCH_ID,0) ?: return

        Log.d("AlarmReceiver on match", "$id")

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.O){

            val notificationManager = context?.getSystemService<NotificationManager>()
            if (notificationManager?.getNotificationChannel(ALARM_NOTIFICATION_CHANNEL_ID) == null) {
                notificationManager?.let {
                    oldNotificationChannelCleanup(it)// cleans up previous notification channel that had sound properties
                }
                NotificationChannel(ALARM_NOTIFICATION_CHANNEL_ID, "Alarm", NotificationManager.IMPORTANCE_HIGH).apply {
                    setBypassDnd(true)
                    setSound(null, null)
                    notificationManager?.createNotificationChannel(this)
                }
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, AlarmActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(MATCH_ID, id)
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, ALARM_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_vector)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(pendingIntent, true)

            try {
                notificationManager?.notify(ALARM_NOTIF_ID, builder.build())
                Log.d("Alarm Success", "notify complete")

            } catch (e: Exception) {
                Log.e("Alarm Exception", e.toString())
                Toast.makeText(context,"Error happened! $e",Toast.LENGTH_LONG).show()
            }

        }else{
            val alarmIntent = Intent(context,AlarmActivity::class.java)

            alarmIntent.putExtra("MATCH_ID",id)
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context?.startActivity(alarmIntent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun oldNotificationChannelCleanup(notificationManager: NotificationManager) {
        notificationManager.deleteNotificationChannel("Alarm")
    }
}