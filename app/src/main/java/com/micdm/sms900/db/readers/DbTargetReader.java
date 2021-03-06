package com.micdm.sms900.db.readers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.CategoryList;
import com.micdm.sms900.data.Target;
import com.micdm.sms900.data.TargetList;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.misc.DateUtils;

import org.joda.time.DateTime;

public class DbTargetReader extends DbReader<TargetList> {

    private final CategoryList _categories;

    public DbTargetReader(DbHelper dbHelper, CategoryList categories) {
        super(dbHelper);
        _categories = categories;
    }

    @Override
    public TargetList read() {
        if (_categories == null) {
            return null;
        }
        SQLiteDatabase db = getDb();
        Cursor cursor = db.rawQuery(
            "SELECT t.id, t.category_id, t.name, t.title, o.created, o.amount " +
            "FROM targets AS t " +
                "INNER JOIN operations AS o ON(o.target_id = t.id) " +
            "GROUP BY t.id " +
            "ORDER BY t.name, o.created DESC", null
        );
        cursor.moveToFirst();
        TargetList targets = new TargetList();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String title = cursor.getString(3);
            DateTime lastPaid = DateUtils.parseForDb(cursor.getString(4));
            int lastAmount = cursor.getInt(5);
            Target target = new Target(id, categoryId == 0 ? null : _categories.getById(categoryId), name, title, lastPaid, lastAmount);
            targets.add(target);
            cursor.moveToNext();
        }
        cursor.close();
        return targets;
    }
}
