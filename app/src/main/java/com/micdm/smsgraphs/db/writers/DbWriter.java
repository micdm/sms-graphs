package com.micdm.smsgraphs.db.writers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.db.DbOpenHelper;

public abstract class DbWriter {

    protected final SQLiteDatabase db;

    public DbWriter(Context context) {
        db = new DbOpenHelper(context).getWritableDatabase();
    }
}
