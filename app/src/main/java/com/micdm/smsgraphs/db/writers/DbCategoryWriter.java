package com.micdm.smsgraphs.db.writers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.db.DbHelper;

public class DbCategoryWriter extends DbWriter<Category> {

    public DbCategoryWriter(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void add(Category category) {
        SQLiteDatabase db = getDb();
        db.insert("categories", null, getValues(category));
    }

    @Override
    public void update(Category category) {
        SQLiteDatabase db = getDb();
        db.update("categories", getValues(category), "id = ?", new String[] {String.valueOf(category.getId())});
    }

    private ContentValues getValues(Category category) {
        ContentValues result = new ContentValues();
        result.put("name", category.getName());
        return result;
    }
}
