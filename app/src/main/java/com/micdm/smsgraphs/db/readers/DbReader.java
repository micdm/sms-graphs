package com.micdm.smsgraphs.db.readers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.db.DbHelper;

public abstract class DbReader<Entity> extends AsyncTaskLoader<Entity> {

    private DbHelper dbHelper;

    public DbReader(Context context, DbHelper dbHelper) {
        super(context);
        this.dbHelper = dbHelper;
    }

    protected SQLiteDatabase getDb() {
        return dbHelper.getReadableDatabase();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
