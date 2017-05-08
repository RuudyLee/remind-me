package com.rudy.remindme;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        0
                );

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .setContentTitle("Remind me!")
                .setContentText("Running. Tap to stop.")
                .addAction(android.R.drawable.ic_media_ff, "Stop", resultPendingIntent);

        mBuilder.setContentIntent(resultPendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationManager.cancelAll();
        Intent intent = new Intent(this, ReminderService.class);
        stopService(intent);
    }

    public void startServiceOnClick(View v) {

        // Get message
        EditText editText = (EditText) findViewById(R.id.et_msg);
        String msg = editText.getText().toString();

        // Get interval value
        Spinner spinner = (Spinner) findViewById(R.id.sp_interval);
        int interval = Integer.parseInt(spinner.getSelectedItem().toString());


        notificationManager.notify(0, mBuilder.build());

        Intent intent = new Intent(this, ReminderService.class);
        intent.putExtra("msg", msg);
        intent.putExtra("interval", interval);
        startService(intent);
    }
}
