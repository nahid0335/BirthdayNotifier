package com.example.birthdaynotifier.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private BirthDateViewModel birthDateViewModel;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        PersistableBundle extras = jobParameters.getExtras();
        String name = extras.getString("Message");
        int id = extras.getInt("Id");
        int month = extras.getInt("Month");
        int day = extras.getInt("Day");
        String time = extras.getString("Time");
        showNotification(id, name);
        Calendar calendar = Calendar.getInstance();
        Calendar currentTime  = Calendar.getInstance();


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) { }

        int year;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
        }else{
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            year = cal.get(Calendar.YEAR);
        }


        calendar.setTime(date);
        calendar.set(Calendar.YEAR,year+1);
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.SECOND, 0);

        long differenceInmillis = calendar.getTimeInMillis()-currentTime.getTimeInMillis();

        ComponentName componentName = new ComponentName(this, NotificationJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(id, componentName)
                .setMinimumLatency(differenceInmillis)
                .setPersisted(true)
                .setOverrideDeadline(differenceInmillis+60*1000)
                .setExtras(extras)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    private void showNotification(int id, String task){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setContentTitle("Happy Birthday To ")
                .setContentText(task)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        notificationManager.notify(id, notification.build());
    }
}
