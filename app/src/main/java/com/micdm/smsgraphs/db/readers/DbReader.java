package com.micdm.smsgraphs.db.readers;

import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.db.DbHelper;

public abstract class DbReader<Entity> {

    private final DbHelper dbHelper;

    public DbReader(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected SQLiteDatabase getDb() {
        return dbHelper.getReadableDatabase();
    }

    public abstract Entity read();
}
