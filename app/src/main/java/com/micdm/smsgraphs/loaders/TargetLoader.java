package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbTargetReader;

public class TargetLoader extends AsyncTaskLoader<TargetList> {

    private final DbHelper dbHelper;

    private final CategoryList categories;
    private TargetList targets;

    public TargetLoader(Context context, DbHelper dbHelper, CategoryList categories) {
        super(context);
        this.dbHelper = dbHelper;
        this.categories = categories;
    }

    @Override
    protected void onStartLoading() {
        if (targets == null) {
            forceLoad();
        } else if (takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(targets);
        }
    }

    @Override
    public TargetList loadInBackground() {
        if (categories == null) {
            return null;
        }
        return (new DbTargetReader(dbHelper, categories)).read();
    }

    @Override
    public void deliverResult(TargetList data) {
        targets = data;
        super.deliverResult(data);
    }
}
