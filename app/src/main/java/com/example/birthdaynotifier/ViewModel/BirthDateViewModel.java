package com.example.birthdaynotifier.ViewModel;

import android.app.Application;

import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.Repository.BirthDateRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BirthDateViewModel extends AndroidViewModel {

    private BirthDateRepository repository;
    private LiveData<List<BirthDate>> allBirthDate;

    public BirthDateViewModel(@NonNull Application application) {
        super(application);

        repository = new BirthDateRepository(application);
        allBirthDate = repository.getAllBirthDate();
    }

    public void insert(BirthDate birthDate) {
        repository.insert(birthDate);
    }

    public void update(BirthDate birthDate) {
        repository.update(birthDate);
    }

    public void delete(BirthDate birthDate) {
        repository.delete(birthDate);
    }

    public void deleteAllBirthDate() {
        repository.deleteAllBirthDate();
    }

    public LiveData<List<BirthDate>> getAllBirthDate() {
        return allBirthDate;
    }
}
