package com.micdm.smsgraphs.db.writers;

import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.db.DbHelper;

public abstract class DbWriter<Entity> {

    private DbHelper dbHelper;

    public DbWriter(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected SQLiteDatabase getDb() {
        return dbHelper.getWritableDatabase();
    }

    public abstract void write(Entity entity);
}
