package com.micdm.smsgraphs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "main";
    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCardTable(db);
        createCategoryTable(db);
        createTargetTable(db);
        createTargetCategoryTable(db);
        createOperationTable(db);
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
                "name TEXT UNIQUE" +
            ")"
        );
    }

    private void createTargetCategoryTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE target_categories (" +
                "target_id INTEGER REFERENCES targets(id)," +
                "category_id INTEGER REFERENCES categories(id)," +
                "PRIMARY KEY (target_id, category_id)" +
            ")"
        );
    }

    private void createOperationTable(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE operations (" +
                "id INTEGER PRIMARY KEY," +
                "card INTEGER REFERENCES cards(id)," +
                "target INTEGER REFERENCES targets(id)," +
                "created DATETIME," +
                "type INTEGER," +
                "amount DECIMAL(10, 2)," +
                "UNIQUE (card, target, created, type, amount)" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previous, int next) {

    }
}
