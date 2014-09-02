package com.micdm.sms900.messages;

import android.content.Context;

import com.micdm.sms900.data.Message;
import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.db.writers.DbMessageWriter;

class MessageLoader {

    public static interface OnLoadListener {
        public void onStartLoad();
        public void onProgress(int total, int current, Message message);
        public void onFinishLoad();
    }

    private final MessageReader _reader;
    private final DbMessageWriter _writer;
    private final OnLoadListener _listener;

    public MessageLoader(Context context, DbHelper dbHelper, final OnLoadListener listener) {
        _reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public void onProgress(int total, int current, Message message) {
                listener.onProgress(total, current, message);
                if (message != null) {
                    _writer.add(message);
                }
            }
        });
        _writer = new DbMessageWriter(dbHelper);
        _listener = listener;
    }

    public void load() {
        _listener.onStartLoad();
        _reader.read();
        _listener.onFinishLoad();
    }
}
