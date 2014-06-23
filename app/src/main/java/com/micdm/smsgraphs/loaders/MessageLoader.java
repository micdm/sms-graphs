package com.micdm.smsgraphs.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;
import com.micdm.smsgraphs.messages.MessageReader;

public class MessageLoader extends AsyncTaskLoader<Void> {

    public static interface OnLoadListener {
        public void onStartLoad();
        public void onProgress(int total, int current);
        public void onFinishLoad();
    }

    private final OnLoadListener listener;

    private final MessageReader reader;
    private final DbOperationWriter writer;

    public MessageLoader(Context context, DbHelper dbHelper, final OnLoadListener listener) {
        super(context);
        this.listener = listener;
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
    }

    @Override
    protected void onForceLoad() {
        listener.onStartLoad();
        super.onForceLoad();
    }

    @Override
    public Void loadInBackground() {
        reader.read();
        return null;
    }

    @Override
    public void deliverResult(Void data) {
        listener.onFinishLoad();
        super.deliverResult(data);
    }
}
