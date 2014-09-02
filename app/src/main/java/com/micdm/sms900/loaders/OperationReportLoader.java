package com.micdm.sms900.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.sms900.data.OperationReport;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.db.readers.DbOperationReportReader;

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
