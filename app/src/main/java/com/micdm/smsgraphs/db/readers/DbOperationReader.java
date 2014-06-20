package com.micdm.smsgraphs.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.misc.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DbOperationReader extends DbReader<MonthOperationList> {

    private final TargetList targets;
    private final Calendar month;

    public DbOperationReader(DbHelper dbHelper, TargetList targets, Calendar month) {
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
            "SELECT id, target_id, created, amount " +
            "FROM operations " +
            "WHERE STRFTIME('%m %Y', created) = ? " +
            "ORDER BY id", new String[] {String.format("%02d %d", month.get(Calendar.MONTH) + 1, month.get(Calendar.YEAR))}
        );
        cursor.moveToFirst();
        List<Operation> operations = new ArrayList<Operation>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            Target target = targets.getById(cursor.getInt(1));
            Calendar created = DateUtils.parseForDb(cursor.getString(2));
            int amount = cursor.getInt(3);
            Operation operation = new Operation(id, target, created, amount);
            operations.add(operation);
            cursor.moveToNext();
        }
        cursor.close();
        return new MonthOperationList(month, operations);
    }
}
