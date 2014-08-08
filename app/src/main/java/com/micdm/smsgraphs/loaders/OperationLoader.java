package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReader;

import org.joda.time.DateTime;

public class OperationLoader extends AsyncTaskLoader<MonthOperationList> {

    private final DbHelper _dbHelper;

    private final TargetList _targets;
    private final DateTime _month;
    private MonthOperationList _operations;

    public OperationLoader(Context context, DbHelper dbHelper, TargetList targets, DateTime month) {
        super(context);
        _dbHelper = dbHelper;
        _targets = targets;
        _month = month;
    }

    @Override
    protected void onStartLoading() {
        if (_operations == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(_operations);
        }
    }

    @Override
    public MonthOperationList loadInBackground() {
        if (_targets == null || _month == null) {
            return null;
        }
        return (new DbOperationReader(_dbHelper, _targets, _month)).read();
    }

    @Override
    public void deliverResult(MonthOperationList data) {
        _operations = data;
        super.deliverResult(data);
    }
}
