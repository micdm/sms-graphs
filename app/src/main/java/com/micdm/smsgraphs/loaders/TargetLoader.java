package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbTargetReader;

public class TargetLoader extends AsyncTaskLoader<TargetList> {

    private final DbHelper _dbHelper;

    private final CategoryList _categories;
    private TargetList _targets;

    public TargetLoader(Context context, DbHelper dbHelper, CategoryList categories) {
        super(context);
        _dbHelper = dbHelper;
        _categories = categories;
    }

    @Override
    protected void onStartLoading() {
        if (_targets == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(_targets);
        }
    }

    @Override
    public TargetList loadInBackground() {
        if (_categories == null) {
            return null;
        }
        return (new DbTargetReader(_dbHelper, _categories)).read();
    }

    @Override
    public void deliverResult(TargetList data) {
        _targets = data;
        super.deliverResult(data);
    }
}
