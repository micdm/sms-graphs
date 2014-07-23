package com.micdm.smsgraphs.db.writers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.db.DbHelper;

public class DbOperationWriter extends DbWriter<Operation> {

    public DbOperationWriter(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void write(Operation operation) {
        SQLiteDatabase db = getDb();
        db.update("operations", getValues(operation), "id = ?", new String[] {String.valueOf(operation.getId())});
    }

    private ContentValues getValues(Operation operation) {
        ContentValues result = new ContentValues();
        result.put("ignored", operation.isIgnored());
        return result;
    }
}
