package com.rudy.remindme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class ReminderService extends Service {
    Handler h;
    Runnable runnable;
    String msg;
    int delay;

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @Override
    public IBinder onBind(Intent intent) {
        // We never bind to this service
        return null;
    }

    @Override
    public void onCreate() {
        h = new Handler();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Reminders: ON", Toast.LENGTH_SHORT).show();

        // Receive the variables
        msg = intent.getStringExtra("msg");
        delay = intent.getIntExtra("interval", 0) * 1000;

        // Create the notification that does nothing
        PendingIntent rIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_pause)
                .setContentTitle(msg)
                .setContentText("Tap to stop alert");

        mBuilder.setContentIntent(rIntent);

        // Repeat the alert at given interval
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                notificationManager.notify(1, mBuilder.build());

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Reminders: OFF", Toast.LENGTH_SHORT).show();
        h.removeCallbacks(runnable);
    }

    public class ReminderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAG", "RESPONSE");
        }
    }
}
