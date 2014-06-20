package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.readers.DbOperationReportReader;

public class OperationReportLoader extends Loader<OperationReport> {

    private final DbOperationReportReader reader;
    private OperationReport report;

    public OperationReportLoader(Context context, DbHelper dbHelper, OnLoadListener onLoadListener) {
        super(context, onLoadListener);
        reader = new DbOperationReportReader(dbHelper);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (report == null || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(report);
        }
    }

    @Override
    public OperationReport loadInBackground() {
        return reader.read();
    }

    @Override
    public void deliverResult(OperationReport report) {
        super.deliverResult(report);
        this.report = report;
    }
}
