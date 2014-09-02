package com.micdm.sms900.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.OperationReport;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.misc.DateUtils;

import org.joda.time.DateTime;

public class DbOperationReportReader extends DbReader<OperationReport> {

    public DbOperationReportReader(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public OperationReport read() {
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT MIN(created), MAX(created) " +
            "FROM operations ", null
        );
        cursor.moveToFirst();
        OperationReport report;
        if (cursor.isNull(0) && cursor.isNull(1)) {
            report = new OperationReport(null, null);
        } else {
            DateTime first = DateUtils.parseForDb(cursor.getString(0));
            DateTime last = DateUtils.parseForDb(cursor.getString(1));
            report = new OperationReport(first, last);
        }
        cursor.close();
        return report;
    }
}
