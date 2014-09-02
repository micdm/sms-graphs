package com.micdm.sms900.messages;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.micdm.sms900.data.Message;
import com.micdm.sms900.misc.Logger;

public class MessageReader {

    public static interface OnMessageListener {
        public void onProgress(int total, int current, Message message);
    }

    public static final String SERVICE_NUMBER = "900";

    private static final Uri INBOX_URI = Uri.parse("content://sms/inbox");
    private static final String PREF_KEY = "last_parsed_message_id";

    private final MessageParser _parser = new MessageParser();

    private final Context _context;
    private final OnMessageListener _listener;

    public MessageReader(Context context, OnMessageListener listener) {
        _context = context;
        _listener = listener;
    }

    public void read() {
        Logger.debug("Reading new SMS messages...");
        Cursor cursor = getCursor();
        cursor.moveToFirst();
        int id = 0;
        int total = cursor.getCount();
        int current = 1;
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            Message message = _parser.parse(cursor.getString(1));
            _listener.onProgress(total, current, message);
            cursor.moveToNext();
            current += 1;
        }
        if (id != 0) {
            setLastParsedMessageId(id);
        }
        cursor.close();
    }

    private Cursor getCursor() {
        String[] fields = new String[] {"_id", "body"};
        String[] where = new String[] {String.valueOf(getLastParsedMessageId()), SERVICE_NUMBER};
        Cursor cursor = _context.getContentResolver().query(INBOX_URI, fields, "_id > ? AND address = ?", where, "_id");
        if (cursor == null) {
            throw new RuntimeException("can not access to messages");
        }
        return cursor;
    }

    private int getLastParsedMessageId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        return prefs.getInt(PREF_KEY, 0);
    }

    private void setLastParsedMessageId(int id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_KEY, id).apply();
    }
}
