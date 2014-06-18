package com.micdm.smsgraphs.db.readers;

import android.content.Context;
import android.database.Cursor;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.misc.DateUtils;

import java.util.Calendar;

public class DbOperationReportReader extends DbReader<OperationReport> {

    public DbOperationReportReader(Context context) {
        super(context);
    }

    @Override
    public OperationReport loadInBackground() {
        Cursor cursor = db.rawQuery(
            "SELECT MIN(created), MAX(created) " +
            "FROM operations ", null
        );
        cursor.moveToFirst();
        OperationReport report = null;
        if (!cursor.isAfterLast()) {
            Calendar first = DateUtils.parseForDb(cursor.getString(0));
            Calendar last = DateUtils.parseForDb(cursor.getString(1));
            report = new OperationReport(first, last);
        }
        cursor.close();
        return report;
    }
}
