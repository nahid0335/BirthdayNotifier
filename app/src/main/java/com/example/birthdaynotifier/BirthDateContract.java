package com.example.birthdaynotifier;

import android.provider.BaseColumns;

public class BirthDateContract {
    private BirthDateContract(){}

    public static class BirthDateTable implements BaseColumns {
        public static final String TABLE_NAME = "birthdate_table";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_NOTIFICATION = "notification";
    }
}
