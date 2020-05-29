package com.example.birthdaynotifier.notification;


import android.app.job.JobParameters;
import android.app.job.JobService;
import com.example.birthdaynotifier.BirthDateSQL;
import com.example.birthdaynotifier.Database.BirthDateSQLDbHelper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        doBackgroundWork(jobParameters);
        return true;
    }

    private void doBackgroundWork(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //----------------------------database inisialization---------------------------------
                final BirthDateSQLDbHelper birthDateSQLDbHelper = BirthDateSQLDbHelper.getInstance(getApplicationContext());
                //sqLiteDatabase = birthDateDbHelper.getWritableDatabase();
                //-------------------------------------------------------------------------------------
                ArrayList<BirthDateSQL> birthDates = birthDateSQLDbHelper.getAllBirthDatesList();


                Date today = new Date();
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(today);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

                int daysInMonth;
                YearMonth yearMonthObject;
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
                for(BirthDateSQL birthDate: birthDates){
                    //Toast.makeText(DataSearch.this,birthDate.getName(),Toast.LENGTH_LONG).show();
                    int notification = birthDate.getNotification();
                    if(notification ==1){
                        int month = birthDate.getMonth();
                        month -=1;
                        if(month == currentMonth){
                            if(birthDate.getDay() == currentDay){
                                String message = birthDate.getName();
                                long id = birthDate.getId();
                                NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
                                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(message);
                                notificationHelper.getManager().notify((int)id, nb.build());
                            }
                        }
                    }
                }
                jobFinished(jobParameters, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {/*
        boolean jobCancelled = true;*/
        return true;
    }
}
