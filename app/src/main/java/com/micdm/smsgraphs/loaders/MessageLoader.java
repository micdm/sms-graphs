package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;
import com.micdm.smsgraphs.messages.MessageReader;

public class MessageLoader extends Loader<Integer> {

    public static interface OnProgressListener {
        public void onProgress(int total, int current);
    }

    private final MessageReader reader;
    private final DbOperationWriter writer;
    private int count;

    public MessageLoader(Context context, DbHelper dbHelper, OnLoadListener onLoadListener, final OnProgressListener onProgressListener) {
        super(context, onLoadListener);
        reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public void onProgress(int total, int current) {
                onProgressListener.onProgress(total, current);
            }
            @Override
            public void onMessage(Message message) {
                writer.write(message);
                count += 1;
            }
        });
        writer = new DbOperationWriter(dbHelper);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Integer loadInBackground() {
        count = 0;
        reader.read();
        return count;
    }
}
