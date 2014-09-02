package com.micdm.sms900.db.writers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.db.DbHelper;

public class DbTargetWriter extends DbWriter<Target> {

    public DbTargetWriter(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void update(Target target) {
        SQLiteDatabase db = getDb();
        db.update("targets", getValues(target), "id = ?", new String[] {String.valueOf(target.getId())});
    }

    private ContentValues getValues(Target target) {
        ContentValues result = new ContentValues();
        Category category = target.getCategory();
        if (category == null)  {
            result.putNull("category_id");
        } else {
            result.put("category_id", category.getId());
        }
        result.put("name", target.getName());
        String title = target.getTitle();
        if (title == null) {
            result.putNull("title");
        } else {
            result.put("title", title);
        }
        return result;
    }
}
