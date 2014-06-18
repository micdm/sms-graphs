package com.micdm.smsgraphs.messages;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MessageLoader {

    public static interface OnMessageListener {
        public boolean onMessage(Message message);
    }

    private static final Uri INBOX_URI = Uri.parse("content://sms/inbox");
    private static final String SERVICE_NUMBER = "900";

    private final Context context;
    private final OnMessageListener listener;
    private Cursor cursor;

    public MessageLoader(Context context, OnMessageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    private Cursor getCursor() {
        String[] fields = new String[] {"body"};
        Cursor cursor = context.getContentResolver().query(INBOX_URI, fields, "address = ?", new String[] {SERVICE_NUMBER}, "_id DESC");
        if (cursor == null) {
            throw new RuntimeException("can not access to messages");
        }
        cursor.moveToFirst();
        return cursor;
    }

    public void load() {
        if (cursor == null) {
            cursor = getCursor();
        }
        while (!cursor.isAfterLast()) {
            Message message = MessageParser.parse(cursor.getString(0));
            if (!listener.onMessage(message)) {
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
}
