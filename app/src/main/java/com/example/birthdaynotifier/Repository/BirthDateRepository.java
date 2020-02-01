package com.example.birthdaynotifier.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.BirthDateDao;
import com.example.birthdaynotifier.Database.BirthDateDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BirthDateRepository {

    private BirthDateDao birthDateDao;
    private LiveData<List<BirthDate>> allBirthDate;

    public BirthDateRepository(Application application) {
        BirthDateDatabase database = BirthDateDatabase.getInstance(application);
        birthDateDao = database.birthDateDao();
        allBirthDate = birthDateDao.getAllBirthdate();
    }

    public void insert(BirthDate birthDate) {
        new InsertBirthDateAsyncTask(birthDateDao).execute(birthDate);
    }

    public void update(BirthDate birthDate) {
        new UpdateBirthDateAsyncTask(birthDateDao).execute(birthDate);
    }

    public void delete(BirthDate birthDate) {
        new DeleteBirthDateAsyncTask(birthDateDao).execute(birthDate);
    }

    public void deleteAllBirthDate() {
        new DeleteAllBirthDatesAsyncTask(birthDateDao).execute();
    }

    public LiveData<List<BirthDate>> getAllBirthDate() {
        return allBirthDate;
    }

    private static class InsertBirthDateAsyncTask extends AsyncTask<BirthDate, Void, Void> {
        private BirthDateDao birthDateDao;

        private InsertBirthDateAsyncTask(BirthDateDao birthDateDao) {
            this.birthDateDao = birthDateDao;
        }

        @Override
        protected Void doInBackground(BirthDate... birthDates) {
            birthDateDao.insert(birthDates[0]);
            return null;
        }
    }

    private static class UpdateBirthDateAsyncTask extends AsyncTask<BirthDate, Void, Void> {
        private BirthDateDao birthDateDao;

        private UpdateBirthDateAsyncTask(BirthDateDao birthDateDao) {
            this.birthDateDao = birthDateDao;
        }

        @Override
        protected Void doInBackground(BirthDate... birthDates) {
            birthDateDao.update(birthDates[0]);
            return null;
        }
    }

    private static class DeleteBirthDateAsyncTask extends AsyncTask<BirthDate, Void, Void> {
        private BirthDateDao birthDateDao;

        private DeleteBirthDateAsyncTask(BirthDateDao birthDateDao) {
            this.birthDateDao = birthDateDao;
        }

        @Override
        protected Void doInBackground(BirthDate... birthDates) {
            birthDateDao.delete(birthDates[0]);
            return null;
        }
    }

    private static class DeleteAllBirthDatesAsyncTask extends AsyncTask<Void, Void, Void> {
        private BirthDateDao birthDateDao;

        private DeleteAllBirthDatesAsyncTask(BirthDateDao birthDateDao) {
            this.birthDateDao = birthDateDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            birthDateDao.deleteAllBirthDate();
            return null;
        }
    }
}
