package com.micdm.smsgraphs.messages;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;

public class MessageConverter extends AsyncTaskLoader<Void> {

    private final MessageLoader loader;
    private final DbOperationWriter writer;

    public MessageConverter(Context context, DbHelper dbHelper) {
        super(context);
        loader = new MessageLoader(context, new MessageLoader.OnMessageListener() {
            @Override
            public boolean onMessage(Message message) {
                return message == null || writer.write(message);
            }
        });
        writer = new DbOperationWriter(dbHelper);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Void loadInBackground() {
        loader.load();
        return null;
    }
}
