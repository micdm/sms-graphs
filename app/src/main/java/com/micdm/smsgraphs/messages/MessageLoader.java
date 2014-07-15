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

    private final MessageReader reader;
    private final DbOperationWriter writer;
    private final OnLoadListener listener;

    public MessageLoader(Context context, DbHelper dbHelper, final OnLoadListener listener) {
        reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public void onProgress(int total, int current) {
                listener.onProgress(total, current);
            }
            @Override
            public void onMessage(Message message) {
                writer.write(message);
            }
        });
        writer = new DbOperationWriter(dbHelper);
        this.listener = listener;
    }

    public void load() {
        listener.onStartLoad();
        reader.read();
        listener.onFinishLoad();
    }
}
