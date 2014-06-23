package com.micdm.smsgraphs;

import android.app.Application;

import com.micdm.smsgraphs.db.DbHelper;

public class CustomApplication extends Application {

    private final DbHelper dbHelper = new DbHelper(this);

    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
