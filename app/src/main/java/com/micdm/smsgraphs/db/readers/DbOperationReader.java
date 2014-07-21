package com.micdm.smsgraphs.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.misc.DateUtils;

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
            "SELECT id, target_id, created, amount " +
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
            Operation operation = new Operation(id, target, created, amount);
            operations.add(operation);
            cursor.moveToNext();
        }
        cursor.close();
        return new MonthOperationList(_month, operations);
    }
}
