package com.micdm.smsgraphs.db.readers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class DbCategoryReader extends DbReader<List<Category>> {

    public DbCategoryReader(Context context, DbHelper dbHelper) {
        super(context, dbHelper);
    }

    @Override
    public List<Category> loadInBackground() {
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT id, name " +
            "FROM categories " +
            "ORDER BY name", null
        );
        cursor.moveToFirst();
        List<Category> categories = new ArrayList<Category>();
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
