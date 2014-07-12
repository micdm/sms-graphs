package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReportReader;

public class OperationReportLoader extends AsyncTaskLoader<OperationReport> {

    private final DbHelper dbHelper;

    private OperationReport report;

    public OperationReportLoader(Context context, DbHelper dbHelper) {
        super(context);
        this.dbHelper = dbHelper;
    }

    @Override
    protected void onStartLoading() {
        if (report == null) {
            forceLoad();
        } else if (takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(report);
        }
    }

    @Override
    public OperationReport loadInBackground() {
        return (new DbOperationReportReader(dbHelper)).read();
    }

    @Override
    public void deliverResult(OperationReport data) {
        report = data;
        super.deliverResult(data);
    }
}
