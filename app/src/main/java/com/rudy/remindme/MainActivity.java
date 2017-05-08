package com.rudy.remindme;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .setContentTitle("Remind me!")
                .setContentText("WHATEVER");

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
        mBuilder.setContentIntent(resultPendingIntent);
    }

    public void startServiceOnClick(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.sp_interval);
        String msg = spinner.getSelectedItem().toString();

        notificationManager.notify(0, mBuilder.build());
        Toast.makeText(getApplicationContext(), "Service Started!", Toast.LENGTH_SHORT)
                .show();
    }
}
