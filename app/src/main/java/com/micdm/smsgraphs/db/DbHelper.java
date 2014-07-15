package com.micdm.smsgraphs.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.micdm.smsgraphs.R;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "main";
    private static final int DB_VERSION = 1;

    private final Context _context;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCardTable(db);
        createCategoryTable(db);
        createTargetTable(db);
        createOperationTable(db);
        addCategories(db);
    }

    private void createCardTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE cards (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT UNIQUE" +
            ")"
        );
    }

    private void createCategoryTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT UNIQUE" +
            ")"
        );
    }

    private void createTargetTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE targets (" +
                "id INTEGER PRIMARY KEY," +
                "category_id INTEGER REFERENCES categories(id)," +
                "name TEXT UNIQUE," +
                "title TEXT" +
            ")"
        );
    }

    private void createOperationTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE operations (" +
                "id INTEGER PRIMARY KEY," +
                "card_id INTEGER REFERENCES cards(id)," +
                "target_id INTEGER REFERENCES targets(id)," +
                "created DATETIME," +
                "amount INTEGER," +
                "UNIQUE (card_id, target_id, created, amount)" +
            ")"
        );
    }

    private void addCategories(SQLiteDatabase db) {
        String[] names = _context.getResources().getStringArray(R.array.categories);
        for (String name: names) {
            ContentValues values = new ContentValues();
            values.put("name", name);
            db.insert("categories", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previous, int next) {

    }
}
