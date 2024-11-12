package com.shaihi.alarmmanagerexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadcastReceiver";
    private final String TIMER_FINISHED_ACTION = "com.shaihi.alarmmanagerexample.TIMER_FINISHED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Log a message when the alarm goes off
        Log.d(TAG, "Timer is up! Alarm triggered.");

        // Send a broadcast to inform MainActivity that the timer is finished
        //Intent timerFinishedIntent = new Intent(TIMER_FINISHED_ACTION);
        //context.sendBroadcast(timerFinishedIntent);
        // Use LocalBroadcastManager to send a broadcast to MainActivity
        Intent timerFinishedIntent = new Intent(TIMER_FINISHED_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(timerFinishedIntent);

        // Use a Handler to post a Toast message if you wish
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "The timer has elapsed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}