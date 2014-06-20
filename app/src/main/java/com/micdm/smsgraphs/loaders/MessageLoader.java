package com.micdm.smsgraphs.loaders;

import android.content.Context;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;
import com.micdm.smsgraphs.messages.MessageReader;

public class MessageLoader extends Loader<Integer> {

    private final MessageReader reader;
    private final DbOperationWriter writer;
    private int count;

    public MessageLoader(Context context, DbHelper dbHelper, OnLoadListener onLoadListener) {
        super(context, onLoadListener);
        reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public boolean onMessage(Message message) {
                if (message == null) {
                    return true;
                }
                if (writer.write(message)) {
                    count += 1;
                    return true;
                }
                return false;
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
