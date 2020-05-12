package com.example.birthdaynotifier.notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.MainActivity;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class DataSearchService extends IntentService {
    BirthDateViewModel birthDateViewModel;


    public DataSearchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        birthDateViewModel = ViewModelProviders.of(MainActivity.this).get(BirthDateViewModel.class);
        birthDateViewModel.getAllBirthDate().observe(this, new Observer<List<BirthDate>>() {
            @Override
            public void onChanged(List<BirthDate> birthDates) {
                Date today = new Date();
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(today);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

                int daysInMonth;
                YearMonth yearMonthObject = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    int year = currentDate.getYear();
                    yearMonthObject = YearMonth.of(year, Calendar.MONTH);
                    daysInMonth = yearMonthObject.lengthOfMonth();
                }else{
                    daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                }


                if(currentDay == daysInMonth){
                    if(currentMonth == 11){
                        currentMonth = -1;
                    }
                    currentMonth +=1;
                    currentDay = 1;

                }else{
                    currentDay+=1;
                }
                for(BirthDate birthDate: Objects.requireNonNull(birthDates)){
                    //Toast.makeText(DataSearch.this,birthDate.getName(),Toast.LENGTH_LONG).show();
                    boolean notification = birthDate.getNotification();
                    if(notification){
                        int month = birthDate.getMonth();
                        month -=1;
                        if(month == currentMonth){
                            if(birthDate.getDay() == currentDay){
                                String message = birthDate.getName();
                                int id = birthDate.getId();
                                NotificationHelper notificationHelper = new NotificationHelper(DataSearch.this);
                                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(message);
                                notificationHelper.getManager().notify(id, nb.build());
                            }
                        }
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, currentMonth);
                calendar.set(Calendar.DAY_OF_MONTH, currentDay);
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND, 0);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                //Toast.makeText(DataSearch.this,currentDay+"-"+currentMonth,Toast.LENGTH_LONG).show();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(DataSearch.this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(DataSearch.this, 1607005, intent, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                ComponentName receiver = new ComponentName(DataSearch.this, AlertReceiver.class);
                PackageManager pm = getPackageManager();

                pm.setComponentEnabledSetting(receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

            }
        });
    }
}
