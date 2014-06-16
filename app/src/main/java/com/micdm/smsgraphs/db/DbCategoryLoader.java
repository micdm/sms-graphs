package com.micdm.smsgraphs.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Category;

import java.util.ArrayList;
import java.util.List;

public class DbCategoryLoader extends AsyncTaskLoader<List<Category>> {

    private final SQLiteDatabase db;

    public DbCategoryLoader(Context context) {
        super(context);
        db = new DbOpenHelper(context).getReadableDatabase();
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public List<Category> loadInBackground() {
        Cursor cursor = db.rawQuery(
            "SELECT id, name " +
            "FROM categories", null
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
