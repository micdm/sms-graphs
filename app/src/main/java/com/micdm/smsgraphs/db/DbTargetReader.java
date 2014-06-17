package com.micdm.smsgraphs.db;

import android.content.Context;
import android.database.Cursor;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;

import java.util.List;

public class DbTargetReader extends DbReader<TargetList> {

    private final List<Category> categories;

    public DbTargetReader(Context context, List<Category> categories) {
        super(context);
        this.categories = categories;
    }

    @Override
    public TargetList loadInBackground() {
        Cursor cursor = db.rawQuery(
            "SELECT id, category_id, name, title " +
            "FROM targets " +
            "ORDER BY name", null
        );
        cursor.moveToFirst();
        TargetList targets = new TargetList();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String title = cursor.getString(3);
            Target target = new Target(id, name, title, categoryId == 0 ? null : getCategoryById(categoryId));
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
