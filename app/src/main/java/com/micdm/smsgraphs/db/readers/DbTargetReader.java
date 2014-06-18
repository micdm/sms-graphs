package com.micdm.smsgraphs.db.readers;

import android.content.Context;
import android.database.Cursor;

import com.micdm.smsgraphs.data.Category;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.misc.DateUtils;

import java.util.Calendar;
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
            "SELECT t.id, t.category_id, t.name, t.title, MAX(o.created) " +
            "FROM targets AS t " +
                "INNER JOIN operations AS o ON(o.target_id = t.id) " +
            "GROUP BY t.id " +
            "ORDER BY t.name", null
        );
        cursor.moveToFirst();
        TargetList targets = new TargetList();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String title = cursor.getString(3);
            Calendar lastPaid = DateUtils.parseForDb(cursor.getString(4));
            Target target = new Target(id, categoryId == 0 ? null : getCategoryById(categoryId), name, title, lastPaid);
            targets.add(target);
            cursor.moveToNext();
        }
        cursor.close();
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
