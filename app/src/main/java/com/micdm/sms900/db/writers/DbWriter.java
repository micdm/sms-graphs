package com.micdm.sms900.db.writers;

import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.db.DbHelper;

public abstract class DbWriter<Entity> {

    private final DbHelper _dbHelper;

    public DbWriter(DbHelper dbHelper) {
        _dbHelper = dbHelper;
    }

    protected SQLiteDatabase getDb() {
        return _dbHelper.getWritableDatabase();
    }

    public void add(Entity entity) {}

    public void update(Entity entity) {}

    public void remove(Entity entity) {}
}
