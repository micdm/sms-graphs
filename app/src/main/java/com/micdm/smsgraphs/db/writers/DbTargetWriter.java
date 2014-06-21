package com.micdm.smsgraphs.db.writers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.db.DbHelper;

public class DbTargetWriter extends DbWriter<Target> {

    public DbTargetWriter(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void write(Target target) {
        SQLiteDatabase db = getDb();
        db.update("targets", getValues(target), "id = ?", new String[] {String.valueOf(target.id)});
    }

    private ContentValues getValues(Target target) {
        ContentValues result = new ContentValues();
        if (target.category == null)  {
            result.putNull("category_id");
        } else {
            result.put("category_id", target.category.id);
        }
        result.put("name", target.name);
        if (target.title == null) {
            result.putNull("title");
        } else {
            result.put("title", target.title);
        }
        return result;
    }
}
