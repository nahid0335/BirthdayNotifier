package com.example.birthdaynotifier;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BirthDateDao {

    @Insert
    void insert(BirthDate birthDate);

    @Update
    void update(BirthDate birthDate);

    @Delete
    void delete(BirthDate birthDate);

    @Query("DELETE FROM birthdate_table")
    void deleteAllBirthDate();

    @Query("SELECT * FROM birthdate_table ORDER BY name ")
    LiveData<List<BirthDate>> getAllBirthdate();
}
