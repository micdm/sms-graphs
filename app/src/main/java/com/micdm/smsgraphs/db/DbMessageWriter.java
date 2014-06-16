package com.micdm.smsgraphs.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.micdm.smsgraphs.messages.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DbMessageWriter {

    private final SQLiteDatabase db;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-d k:m:s");

    public DbMessageWriter(Context context) {
        db = new DbOpenHelper(context).getWritableDatabase();
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
        result.put("name", message.getTarget());
        return result;
    }

    private long writeOperation(Message message, long cardRowId, long targetRowId) {
        return db.insert("operations", null, getMessageValues(message, cardRowId, targetRowId));
    }

    private ContentValues getMessageValues(Message message, long cardRowId, long targetRowId) {
        ContentValues result = new ContentValues();
        result.put("card", cardRowId);
        result.put("target", targetRowId);
        result.put("created", formatter.format(message.getCreated()));
        result.put("type", getMessageType(message.getType()));
        result.put("amount", message.getAmount().toString());
        return result;
    }

    private int getMessageType(Message.Type type) {
        switch (type) {
            case WITHDRAWAL:
                return 0;
            case PURCHASE:
                return 1;
            case TRANSFER:
                return 2;
            default:
                throw new RuntimeException(String.format("unknown message type %s", type));
        }
    }
}
