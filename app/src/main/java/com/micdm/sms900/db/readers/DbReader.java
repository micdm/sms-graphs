package com.micdm.sms900.db.readers;

import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.db.DbHelper;

public abstract class DbReader<Entity> {

    private final DbHelper _dbHelper;

    public DbReader(DbHelper dbHelper) {
        _dbHelper = dbHelper;
    }

    protected SQLiteDatabase getDb() {
        return _dbHelper.getReadableDatabase();
    }

    public abstract Entity read();
}
