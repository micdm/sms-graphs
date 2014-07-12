package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReader;

import org.joda.time.DateTime;

public class OperationLoader extends AsyncTaskLoader<MonthOperationList> {

    private final DbHelper dbHelper;

    private final TargetList targets;
    private final DateTime month;
    private MonthOperationList operations;

    public OperationLoader(Context context, DbHelper dbHelper, TargetList targets, DateTime month) {
        super(context);
        this.dbHelper = dbHelper;
        this.targets = targets;
        this.month = month;
    }

    @Override
    protected void onStartLoading() {
        if (operations == null) {
            forceLoad();
        } else if (takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(operations);
        }
    }

    @Override
    public MonthOperationList loadInBackground() {
        if (targets == null || month == null) {
            return null;
        }
        return (new DbOperationReader(dbHelper, targets, month)).read();
    }

    @Override
    public void deliverResult(MonthOperationList data) {
        operations = data;
        super.deliverResult(data);
    }
}
