package com.example.birthdaynotifier.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.BirthDateDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {BirthDate.class}, version = 2)
public abstract class BirthDateDatabase extends RoomDatabase {

    private static BirthDateDatabase instance;

    public abstract BirthDateDao birthDateDao();

    public static synchronized BirthDateDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BirthDateDatabase.class, "birthdate_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private BirthDateDao birthDateDao;

        private PopulateDbAsyncTask(BirthDateDatabase db) {
            birthDateDao = db.birthDateDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            birthDateDao.insert(new BirthDate("Title 1", "10:40 PM", "12-02-2020",true));
            birthDateDao.insert(new BirthDate("Title 2", "10:41 PM", "01-02-2020",false));
            birthDateDao.insert(new BirthDate("Title 3", "10:42 PM", "01-01-2020",true));
            return null;
        }
    }
}
