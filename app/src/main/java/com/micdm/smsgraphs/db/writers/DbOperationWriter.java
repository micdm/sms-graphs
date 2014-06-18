package com.micdm.smsgraphs.db.writers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.misc.DateUtils;

public class DbOperationWriter extends DbWriter {

    public DbOperationWriter(Context context) {
        super(context);
    }

    public boolean write(Message message) {
        long cardRowId = writeCard(message);
        long targetRowId = writeTarget(message);
        return writeOperation(message, cardRowId, targetRowId) != -1;
    }

    private long writeCard(Message message) {
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

    private long writeTarget(Message message) {
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

    private long writeOperation(Message message, long cardRowId, long targetRowId) {
        try {
            return db.insertOrThrow("operations", null, getMessageValues(message, cardRowId, targetRowId));
        } catch (SQLException e) {
            return -1;
        }
    }

    private ContentValues getMessageValues(Message message, long cardRowId, long targetRowId) {
        ContentValues result = new ContentValues();
        result.put("card_id", cardRowId);
        result.put("target_id", targetRowId);
        result.put("created", DateUtils.formatForDb(message.getCreated()));
        result.put("amount", message.getAmount().toString());
        return result;
    }
}
