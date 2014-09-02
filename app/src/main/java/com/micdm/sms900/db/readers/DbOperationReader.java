package com.micdm.sms900.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.MonthOperationList;
import com.micdm.sms900.data.Operation;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.data.TargetList;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.misc.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class DbOperationReader extends DbReader<MonthOperationList> {

    private final TargetList _targets;
    private final DateTime _month;

    public DbOperationReader(DbHelper dbHelper, TargetList targets, DateTime month) {
        super(dbHelper);
        _targets = targets;
        _month = month;
    }

    @Override
    public MonthOperationList read() {
        if (_targets == null || _month == null) {
            return null;
        }
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT id, target_id, created, amount, ignored " +
            "FROM operations " +
            "WHERE STRFTIME('%m %Y', created) = ? " +
            "ORDER BY id", new String[] {String.format("%02d %d", _month.getMonthOfYear(), _month.getYear())}
        );
        cursor.moveToFirst();
        List<Operation> operations = new ArrayList<Operation>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            Target target = _targets.getById(cursor.getInt(1));
            DateTime created = DateUtils.parseForDb(cursor.getString(2));
            int amount = cursor.getInt(3);
            boolean isIgnored = (cursor.getInt(4) == 1);
            Operation operation = new Operation(id, target, created, amount, isIgnored);
            operations.add(operation);
            cursor.moveToNext();
        }
        cursor.close();
        return new MonthOperationList(_month, operations);
    }
}
