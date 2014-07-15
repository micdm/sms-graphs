package com.micdm.smsgraphs.messages;

import android.content.Context;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;

class MessageLoader {

    public static interface OnLoadListener {
        public void onStartLoad();
        public void onProgress(int total, int current);
        public void onFinishLoad();
    }

    private final MessageReader _reader;
    private final DbOperationWriter _writer;
    private final OnLoadListener _listener;

    public MessageLoader(Context context, DbHelper dbHelper, final OnLoadListener listener) {
        _reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public void onProgress(int total, int current) {
                listener.onProgress(total, current);
            }
            @Override
            public void onMessage(Message message) {
                _writer.write(message);
            }
        });
        _writer = new DbOperationWriter(dbHelper);
        _listener = listener;
    }

    public void load() {
        _listener.onStartLoad();
        _reader.read();
        _listener.onFinishLoad();
    }
}
