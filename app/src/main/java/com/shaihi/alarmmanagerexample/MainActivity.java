package com.shaihi.alarmmanagerexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.BroadcastReceiver;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
    private TextView timerText;
    private Button startButton;
    private AlarmManager alarmManager;
    private final String TIMER_FINISHED_ACTION = "com.shaihi.alarmmanagerexample.TIMER_FINISHED";

    // Receiver to listen for timer finished broadcasts
    //This private Localreceiver can run on the UI thread.
    // We "fire" this local message in the global AlarmBroadcastReceiver
    private final BroadcastReceiver timerFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the TextView when the timer finishes
            timerText.setText("Timer is up!");
            timerText.setTextColor(Color.RED);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timerFinishedReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (!alarmManager.canScheduleExactAlarms()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
        }

        // Register the local receiver to listen for TIMER_FINISHED_ACTION
        IntentFilter filter = new IntentFilter(TIMER_FINISHED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(timerFinishedReceiver, filter);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown();
            }
        });
    }

    private void startCountdown() {
        // Set an alarm to trigger after 10 seconds (example time)
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        // Set an exact alarm for 10 seconds from now
        long triggerAtMillis = System.currentTimeMillis() + 10000; // 10 seconds
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmIntent);
        //long triggerTime = System.currentTimeMillis() + 10000; // 10 seconds from now
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        timerText.setText("Timer started for 10 seconds");
    }
}