package com.bashkevich.sportalarmclock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bashkevich.sportalarmclock.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra("MATCH_ID",0) ?: return

        Log.d("AlarmReceiver on match", "$id")


        val alarmIntent = Intent(context,AlarmActivity::class.java)

        alarmIntent.putExtra("MATCH_ID",id)
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context?.startActivity(alarmIntent)
    }
}