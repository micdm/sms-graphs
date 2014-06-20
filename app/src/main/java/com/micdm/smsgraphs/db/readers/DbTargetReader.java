package com.micdm.smsgraphs.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.data.CategoryList;
import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.misc.DateUtils;

import java.util.Calendar;

public class DbTargetReader extends DbReader<TargetList> {

    private final CategoryList categories;

    public DbTargetReader(DbHelper dbHelper, CategoryList categories) {
        super(dbHelper);
        this.categories = categories;
    }

    @Override
    public TargetList read() {
        if (categories == null) {
            return null;
        }
        SQLiteDatabase db = getDb();
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
            Target target = new Target(id, categoryId == 0 ? null : categories.getById(categoryId), name, title, lastPaid);
            targets.add(target);
            cursor.moveToNext();
        }
        cursor.close();
        return targets;
    }
}
