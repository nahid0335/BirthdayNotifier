package com.example.birthdaynotifier.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.birthdaynotifier.BirthDateContract;
import com.example.birthdaynotifier.BirthDateContract.*;
import com.example.birthdaynotifier.BirthDateSQL;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class BirthDateSQLDbHelper extends SQLiteOpenHelper {
    private static BirthDateSQLDbHelper mInstance = null;
    private static final String DATABASE_NAME = "BirthDateSQLDatabase.db";
    private static final int DATABASE_VERSION = 15;

    private SQLiteDatabase db;

    public static BirthDateSQLDbHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new BirthDateSQLDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public BirthDateSQLDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        final String SQL_CREATE_BIRTHDATE_TABLE = "CREATE TABLE " +
                BirthDateContract.BirthDateTable.TABLE_NAME + " ( " +
                BirthDateTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BirthDateTable.COLUMN_NAME + " TEXT NOT NULL, " +
                BirthDateTable.COLUMN_DAY + " INTEGER NOT NULL, " +
                BirthDateTable.COLUMN_MONTH + " INTEGER NOT NULL, " +
                BirthDateTable.COLUMN_NOTIFICATION + " INTEGER NOT NULL" +
                ")";
        db.execSQL(SQL_CREATE_BIRTHDATE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BirthDateTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public void insertBirthDate(BirthDateSQL birthDate) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BirthDateTable.COLUMN_NAME, birthDate.getName());
        contentValues.put(BirthDateTable.COLUMN_DAY, birthDate.getDay());
        contentValues.put(BirthDateTable.COLUMN_MONTH, birthDate.getMonth());
        contentValues.put(BirthDateTable.COLUMN_NOTIFICATION, birthDate.getNotification());
        db.insert(BirthDateTable.TABLE_NAME, null, contentValues);
        db.close();
    }


    public ArrayList<BirthDateSQL> getAllBirthDatesList() {
        ArrayList<BirthDateSQL> BirthDateList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BirthDateTable.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                BirthDateSQL birthDate = new BirthDateSQL();
                birthDate.setId(cursor.getInt(cursor.getColumnIndex(BirthDateTable._ID)));
                birthDate.setName(cursor.getString(cursor.getColumnIndex(BirthDateTable.COLUMN_NAME)));
                birthDate.setDay(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_DAY)));
                birthDate.setMonth(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_MONTH)));
                birthDate.setNotification(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_NOTIFICATION)));
                BirthDateList.add(birthDate);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return BirthDateList;
    }


    public Cursor getCursorFromList(List<BirthDateSQL> birthDates) {
        MatrixCursor cursor = new MatrixCursor(
                new String[] {"_id", "name", "day", "month", "notification"}
        );

        for ( BirthDateSQL birthDate : birthDates ) {
            cursor.newRow()
                    .add("_id", birthDate.getId())
                    .add("name", birthDate.getName())
                    .add("day", birthDate.getDay())
                    .add("month", birthDate.getDay())
                    .add("notification", birthDate.getNotification());
        }

        return cursor;
    }



    public Cursor getAllBirthdates() {
        return db.query(
                BirthDateTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                BirthDateTable._ID
        );
    }


    public void deleteBirthDate(long id) {
        db = this.getWritableDatabase();
        db.delete(BirthDateTable.TABLE_NAME,
                BirthDateTable._ID + "=" + id, null);
    }

    public void deleteAllBirthDate() {
        db = this.getWritableDatabase();
        db.delete(BirthDateTable.TABLE_NAME,
                null, null);
    }

    public void updateBirthDate(long birthDateId, BirthDateSQL birthDate) {
        db = this.getWritableDatabase();
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  "+BirthDateTable.TABLE_NAME+" SET name ='"+ birthDate.getName() + "', day ='" + birthDate.getDay()+ "', month ='"+ birthDate.getMonth() + "', notification ='"+ birthDate.getNotification() + "'  WHERE _id='" + birthDateId + "'");
    }


    public BirthDateSQL getSingleBirthDate(long id){
        db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + BirthDateTable.TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query, null);

        BirthDateSQL birthDate = new BirthDateSQL();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            birthDate.setName(cursor.getString(cursor.getColumnIndex(BirthDateTable.COLUMN_NAME)));
            birthDate.setDay(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_DAY)));
            birthDate.setMonth(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_MONTH)));
            birthDate.setNotification(cursor.getInt(cursor.getColumnIndex(BirthDateTable.COLUMN_NOTIFICATION)));
        }

        cursor.close();
        return birthDate;
    }

}
