package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReader;

import java.util.Calendar;

public class OperationLoader extends Loader<MonthOperationList> {

    private final DbOperationReader reader;
    private MonthOperationList operations;

    public OperationLoader(Context context, DbHelper dbHelper, TargetList targets, Calendar month, OnLoadListener onLoadListener) {
        super(context, onLoadListener);
        reader = new DbOperationReader(dbHelper, targets, month);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (operations == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(operations);
        }
    }

    @Override
    public MonthOperationList loadInBackground() {
        return reader.read();
    }

    @Override
    public void deliverResult(MonthOperationList operations) {
        super.deliverResult(operations);
        this.operations = operations;
    }
}
