package com.example.integration;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATIONID = "notification-id";
    public static String NOTIFICATION = "notification";
    Activity context;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(MainActivity.NOTIFICATION_CHANNEL_ID,"NOTIFICATION_CHANNEL_NAME",importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        int id = intent.getIntExtra(NOTIFICATIONID,0);
        assert notificationManager != null;
        notificationManager.notify(id,notification);


        String action = intent.getStringExtra("action");
        if(action.equals("action1")){
            performAction1();
        }
        else if(action.equals("action2")){
            performAction2();
        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }
    public void performAction1(){
        Intent intent = new Intent(context.getApplicationContext(),Parcel_not.class);
        context.startActivity(intent);
    }
    public void performAction2(){
        Intent intent = new Intent(context.getApplicationContext(),Parcel_e.class);
        context.startActivity(intent);
    }
}