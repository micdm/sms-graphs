package com.micdm.smsgraphs.db;

import android.content.Context;
import android.database.Cursor;

import com.micdm.smsgraphs.data.Category;

import java.util.ArrayList;
import java.util.List;

public class DbCategoryReader extends DbReader<List<Category>> {

    public DbCategoryReader(Context context) {
        super(context);
    }

    @Override
    public List<Category> loadInBackground() {
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
        return categories;
    }
}
