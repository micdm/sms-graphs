package com.micdm.smsgraphs.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class DbOperationReader extends DbReader<MonthOperationList> {

    private final TargetList targets;
    private final DateTime month;

    public DbOperationReader(DbHelper dbHelper, TargetList targets, DateTime month) {
        super(dbHelper);
        this.targets = targets;
        this.month = month;
    }

    @Override
    public MonthOperationList read() {
        if (targets == null || month == null) {
            return null;
        }
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT target_id, amount " +
            "FROM operations " +
            "WHERE STRFTIME('%m %Y', created) = ? " +
            "ORDER BY id", new String[] {String.format("%02d %d", month.getMonthOfYear(), month.getYear())}
        );
        cursor.moveToFirst();
        List<Operation> operations = new ArrayList<Operation>();
        while (!cursor.isAfterLast()) {
            Target target = targets.getById(cursor.getInt(0));
            int amount = cursor.getInt(1);
            Operation operation = new Operation(target, amount);
            operations.add(operation);
            cursor.moveToNext();
        }
        cursor.close();
        return new MonthOperationList(month, operations);
    }
}
