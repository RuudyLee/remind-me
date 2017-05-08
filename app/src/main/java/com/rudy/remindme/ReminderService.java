package com.rudy.remindme;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {
    // Device Services
    private NotificationManager mNotificationManager;
    private Vibrator mVibrator;
    private AlarmManager mAlarmManager;

    ReminderReceiver mReminderReceiver;
    private NotificationCompat.Builder mBuilder;

    // Data
    String msg;
    private int delay;

    // ID that intent fires
    private final String START_ALARM = "start_alarm";
    private final String STOP_ALARM = "stop_alarm";

    @Override
    public IBinder onBind(Intent intent) {
        // We never bind to this service
        return null;
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Setup the broadcast receiver
        mReminderReceiver = new ReminderReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addAction(STOP_ALARM);
        intentFilter.addAction(START_ALARM);
        registerReceiver(mReminderReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Reminders: ON", Toast.LENGTH_SHORT).show();

        // Receive the variables
        msg = intent.getStringExtra("msg");
        delay = intent.getIntExtra("interval", 10) * 1000;

        // Create the notification
        Intent notifyIntent = new Intent(STOP_ALARM);
        PendingIntent stopIntent = PendingIntent.getBroadcast(this, 0, notifyIntent, 0);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_pause)
                .setContentTitle(msg)
                .setContentText("Tap to stop alert")
                .setAutoCancel(true);

        mBuilder.setContentIntent(stopIntent);

        // Setup the repeating alarm
        Intent alarmIntent = new Intent(START_ALARM);
        PendingIntent startIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), delay, startIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Reminders: OFF", Toast.LENGTH_SHORT).show();
        mVibrator.cancel();
    }

    public class ReminderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
            String action = intent.getAction();
            switch (action) {
                case STOP_ALARM:
                    v.cancel();
                    break;
                case START_ALARM:
                    long pattern[] = {1000, 1000};
                    v.vibrate(pattern, 0);
                    mNotificationManager.notify(1, mBuilder.build());
                    break;
            }
        }
    }
}
