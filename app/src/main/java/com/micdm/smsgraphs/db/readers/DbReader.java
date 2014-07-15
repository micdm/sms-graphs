package com.micdm.smsgraphs.db.readers;

import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.db.DbHelper;

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
