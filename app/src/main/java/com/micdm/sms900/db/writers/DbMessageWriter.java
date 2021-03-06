package com.micdm.sms900.db.writers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.sms900.data.Message;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.misc.DateUtils;
import com.micdm.sms900.misc.Logger;

public class DbMessageWriter extends DbWriter<Message> {

    public DbMessageWriter(DbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public void add(Message message) {
        Logger.debug("Writing out new operation...");
        SQLiteDatabase db = getDb();
        long cardRowId = writeCard(db, message);
        long targetRowId = writeTarget(db, message);
        writeOperation(db, message, cardRowId, targetRowId);
    }

    private long writeCard(SQLiteDatabase db, Message message) {
        ContentValues values = getCardValues(message);
        try {
            return db.insertOrThrow("cards", null, values);
        } catch (SQLException e) {
            Cursor cursor = db.query("cards", new String[] {"id"}, "name = ?", new String[] {values.getAsString("name")}, null, null, null);
            cursor.moveToFirst();
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
    }

    private ContentValues getCardValues(Message message) {
        ContentValues result = new ContentValues();
        result.put("name", message.getCard());
        return result;
    }

    private long writeTarget(SQLiteDatabase db, Message message) {
        ContentValues values = getTargetValues(message);
        try {
            return db.insertOrThrow("targets", null, getTargetValues(message));
        } catch (SQLException e) {
            Cursor cursor = db.query("targets", new String[] {"id"}, "name = ?", new String[] {values.getAsString("name")}, null, null, null);
            cursor.moveToFirst();
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
    }

    private ContentValues getTargetValues(Message message) {
        ContentValues result = new ContentValues();
        result.putNull("category_id");
        result.put("name", message.getTarget());
        return result;
    }

    private void writeOperation(SQLiteDatabase db, Message message, long cardRowId, long targetRowId) {
        db.insertWithOnConflict("operations", null, getMessageValues(message, cardRowId, targetRowId), SQLiteDatabase.CONFLICT_IGNORE);
    }

    private ContentValues getMessageValues(Message message, long cardRowId, long targetRowId) {
        ContentValues result = new ContentValues();
        result.put("card_id", cardRowId);
        result.put("target_id", targetRowId);
        result.put("created", DateUtils.formatForDb(message.getCreated()));
        result.put("amount", message.getAmount());
        return result;
    }
}
