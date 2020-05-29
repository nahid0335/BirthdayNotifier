package com.example.birthdaynotifier.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = "MainActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // Set the alarm here.
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND, 0);
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1607005, intent, 0);

            assert alarmManager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            ComponentName receiver = new ComponentName(context, AlertReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }else{
            // Set the alarm here.
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            /*if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }*/


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intent = new Intent(context, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1607005, intent, 0);

            assert alarmManager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            ComponentName receiver = new ComponentName(context, AlertReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);




            ComponentName componentName = new ComponentName(context, NotificationJobService.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setRequiresCharging(false)
                    .setMinimumLatency(1000)
                    .setOverrideDeadline(60*1000)
                    .setPersisted(true)
                    .build();
            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            assert scheduler != null;
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
            } else {
                Log.d(TAG, "Job scheduling failed");
            }


/*
            intent = new Intent(context, DataSearchService.class);
            context.startService(intent);*/
        }
    }
}
