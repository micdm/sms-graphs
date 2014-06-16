package com.micdm.smsgraphs.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.OutcomeTarget;

import java.util.ArrayList;
import java.util.List;

public class DbTargetLoader extends AsyncTaskLoader<List<OutcomeTarget>> {

    private final List<Category> categories;
    private final SQLiteDatabase db;

    public DbTargetLoader(Context context, List<Category> categories) {
        super(context);
        this.categories = categories;
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
    public List<OutcomeTarget> loadInBackground() {
        Cursor cursor = db.rawQuery(
            "SELECT id, category_id, name " +
            "FROM targets " +
            "ORDER BY name", null
        );
        cursor.moveToFirst();
        List<OutcomeTarget> targets = new ArrayList<OutcomeTarget>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            OutcomeTarget target = new OutcomeTarget(id, name, categoryId == 0 ? null : getCategoryById(categoryId));
            targets.add(target);
            cursor.moveToNext();
        }
        return targets;
    }

    private Category getCategoryById(Integer id) {
        for (Category category: categories) {
            if (category.id == id) {
                return category;
            }
        }
        throw new RuntimeException(String.format("cannot find category %s", id));
    }
}
