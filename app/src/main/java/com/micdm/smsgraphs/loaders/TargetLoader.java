package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbTargetReader;

public class TargetLoader extends Loader<TargetList> {

    private final DbTargetReader reader;
    private TargetList targets;

    public TargetLoader(Context context, DbHelper dbHelper, CategoryList categories, OnLoadListener onLoadListener) {
        super(context, onLoadListener);
        reader = new DbTargetReader(dbHelper, categories);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (targets == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(targets);
        }
    }

    @Override
    public TargetList loadInBackground() {
        return reader.read();
    }

    @Override
    public void deliverResult(TargetList targets) {
        super.deliverResult(targets);
        this.targets = targets;
    }
}
