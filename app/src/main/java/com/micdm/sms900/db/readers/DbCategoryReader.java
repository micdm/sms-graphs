package com.micdm.sms900.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.Category;
import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.db.DbHelper;

public class DbCategoryReader extends DbReader<CategoryList> {

    public DbCategoryReader(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public CategoryList read() {
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT id, name " +
            "FROM categories " +
            "ORDER BY name", null
        );
        cursor.moveToFirst();
        CategoryList categories = new CategoryList();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = new Category(id, name);
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }
}
