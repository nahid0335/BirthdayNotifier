package com.example.birthdaynotifier.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class AlertReceiver extends BroadcastReceiver {
    BirthDateViewModel birthDateViewModel;

    @Override
    public void onReceive(Context context, Intent intent) {
        //birthDateViewModel = ViewModelProviders.of().get(BirthDateViewModel.class);

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            // Set the alarm here.
            List<BirthDate> birthDates = birthDateViewModel.getAllBirthDate().getValue();
            for(BirthDate birthDate :birthDates) {
                int id = birthDate.getId();
                String name = birthDate.getName();
                boolean notificatiion = birthDate.getNotification();
                String time = birthDate.getTime();
                int month = birthDate.getMonth();
                int day = birthDate.getDay();

                if(notificatiion) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    Date date = null;
                    try {
                        date = sdf.parse(time);
                    } catch (ParseException e) {
                    }
                    Calendar calendar = Calendar.getInstance();

                    int year;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate currentDate = LocalDate.now();
                        year = currentDate.getYear();
                    } else {
                        Date today = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(today);
                        year = cal.get(Calendar.YEAR);
                    }


                    calendar.setTime(date);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.SECOND, 0);
                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.YEAR, 1);
                    }

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    intent = new Intent(context, AlertReceiver.class);
                    intent.putExtra("message",name);
                    intent.putExtra("Id",id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

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
                }
            }
        }else{
            //String message = intent.getStringExtra("message");
            /*String message = "Your friend ! please wish ..";
            int id = intent.getIntExtra("Id",1);
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification(message);
            notificationHelper.getManager().notify(id, nb.build());

            */
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"wk");
            //Acquire the lock
            wl.acquire();
            intent = new Intent(context, DataSearch.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            wl.release();
        }

        /*String message = intent.getStringExtra("message");
        int id = intent.getIntExtra("Id",1);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(message);
        notificationHelper.getManager().notify(id, nb.build());*/
    }
}
