package com.micdm.smsgraphs.messages;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.micdm.smsgraphs.db.DbMessageWriter;

public class MessageConverter extends AsyncTaskLoader<Void> {

    private final MessageLoader loader;
    private final DbMessageWriter writer;

    public MessageConverter(Context context) {
        super(context);
        loader = new MessageLoader(context, new MessageLoader.OnMessageListener() {
            @Override
            public boolean onMessage(Message message) {
                return message == null || writer.write(message);
            }
        });
        writer = new DbMessageWriter(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public Void loadInBackground() {
        loader.load();
        return null;
    }
}
