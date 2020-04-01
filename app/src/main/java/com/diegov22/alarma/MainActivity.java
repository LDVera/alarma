package com.diegov22.alarma;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);

        //set up the notification broadcast intent
        Intent notifyIntent = new Intent(this, alarmReceiver.class);

        //check if the alarm is already set, and check the toggle accordingly
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

        //set up the pendingintent fot alarmanager
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compundButton, boolean isChecked){
                String toastessage;
                if (isChecked){
                    long triggerTime = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

                    //If the toglle is turned on, set the repeating alarm with a 15 minutes interval
                    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);

                    //set the toast message for the "on" cas
                    toastessage = getString(R.string.alarm_on_toast);
                }else{
                    //cancel the alarm and notification if the alarm is turned off
                    alarmManager.cancel(notifyPendingIntent);
                    mNotificationManager.cancelAll();

                    //set the toast message for the "off" cas
                    toastessage = getString(R.string.alarm_off_toast);
                }
                Toast.makeText(MainActivity.this, toastessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
