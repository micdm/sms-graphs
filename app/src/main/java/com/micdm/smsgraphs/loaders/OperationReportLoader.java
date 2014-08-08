package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReportReader;

public class OperationReportLoader extends AsyncTaskLoader<OperationReport> {

    private final DbHelper _dbHelper;

    private OperationReport _report;

    public OperationReportLoader(Context context, DbHelper dbHelper) {
        super(context);
        _dbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        if (_report == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(_report);
        }
    }

    @Override
    public OperationReport loadInBackground() {
        return (new DbOperationReportReader(_dbHelper)).read();
    }

    @Override
    public void deliverResult(OperationReport data) {
        _report = data;
        super.deliverResult(data);
    }
}
