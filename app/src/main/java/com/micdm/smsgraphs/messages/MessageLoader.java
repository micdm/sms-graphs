package com.micdm.smsgraphs.messages;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.micdm.smsgraphs.data.Message;

public class MessageLoader {

    public static interface OnMessageListener {
        public boolean onMessage(Message message);
    }

    private static final Uri INBOX_URI = Uri.parse("content://sms/inbox");
    private static final String SERVICE_NUMBER = "900";

    private final Context context;
    private final OnMessageListener listener;

    public MessageLoader(Context context, OnMessageListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void load() {
        Cursor cursor = getCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = MessageParser.parse(cursor.getString(0));
            if (!listener.onMessage(message)) {
                break;
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private Cursor getCursor() {
        String[] fields = new String[] {"body"};
        Cursor cursor = context.getContentResolver().query(INBOX_URI, fields, "address = ?", new String[] {SERVICE_NUMBER}, "_id DESC");
        if (cursor == null) {
            throw new RuntimeException("can not access to messages");
        }
        return cursor;
    }
}
