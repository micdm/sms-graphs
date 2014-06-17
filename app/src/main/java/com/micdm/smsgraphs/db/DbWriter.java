package com.micdm.smsgraphs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbWriter {

    protected final SQLiteDatabase db;

    public DbWriter(Context context) {
        db = new DbOpenHelper(context).getWritableDatabase();
    }
}
