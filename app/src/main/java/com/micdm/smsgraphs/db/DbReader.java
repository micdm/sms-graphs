package com.micdm.smsgraphs.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbReader<Entity> extends AsyncTaskLoader<Entity> {

    protected final SQLiteDatabase db;

    public DbReader(Context context) {
        super(context);
        db = new DbOpenHelper(context).getReadableDatabase();
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }
}
