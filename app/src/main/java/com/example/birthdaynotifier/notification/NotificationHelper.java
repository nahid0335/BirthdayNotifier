package com.example.birthdaynotifier.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import com.example.birthdaynotifier.MainActivity;
import com.example.birthdaynotifier.R;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {


    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager notificationManager;


    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setVibrationPattern(new long[] { 100, 1000, 100, 1000, 100 });
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }


    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification(String text) {
        Intent resultintent = new Intent(this, MainActivity.class);
        PendingIntent resultpendingIntent = PendingIntent.getActivity(this,11604013,resultintent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Happy Birthday To ")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setAutoCancel(true)
                .setContentIntent(resultpendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 100, 1000, 100, 1000, 100 })
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
    }
}
