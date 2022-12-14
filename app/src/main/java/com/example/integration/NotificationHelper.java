package com.example.integration;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import kotlin.text.MatchNamedGroupCollection;

public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "Channel 1";
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "Channel 2";
    public static final String channel3ID = "channel3ID";
    public static final String channel3Name = "Channel 3";
    private NotificationManager mManager;
    public NotificationHelper(Context base){
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel1 = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel2);

        NotificationChannel channel3 = new NotificationChannel(channel3ID,channel3Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel3.enableLights(true);
        channel3.enableVibration(true);
        channel3.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel3.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel3);
    }

    public NotificationManager getManager(){
        if(mManager ==null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        int requestID = (int) System.currentTimeMillis();
        Intent resultIntent = new Intent(this,MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, requestID, resultIntent, PendingIntent.FLAG_MUTABLE);
        return  new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_one)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        int requestID = (int) System.currentTimeMillis();
        Intent resultIntent = new Intent(this,Parcel_e.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, requestID, resultIntent, PendingIntent.FLAG_MUTABLE);

        return  new NotificationCompat.Builder(getApplicationContext(),channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_two);
    }
    public NotificationCompat.Builder getChannel3Notification(String title, String message){
        int requestID = (int) System.currentTimeMillis();
        Intent resultIntent = new Intent(this,Parcel_not.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, requestID, resultIntent, PendingIntent.FLAG_MUTABLE);

        return  new NotificationCompat.Builder(getApplicationContext(),channel3ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_three)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
