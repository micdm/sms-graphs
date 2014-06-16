package com.micdm.smsgraphs;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.micdm.smsgraphs.messages.MessageConverter;

public class MainActivity extends Activity {

    private static final int MESSAGE_CONVERTER_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        convert();
    }

    private void convert() {
        getLoaderManager().initLoader(MESSAGE_CONVERTER_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Void>() {
            @Override
            public Loader<Void> onCreateLoader(int id, Bundle params) {
                if (id == MESSAGE_CONVERTER_LOADER_ID) {
                    return getMessageConverter();
                }
                return null;
            }
            @Override
            public void onLoadFinished(Loader<Void> loader, Void result) {

            }
            @Override
            public void onLoaderReset(Loader<Void> loader) {

            }
        });
    }

    private MessageConverter getMessageConverter() {
        return new MessageConverter(this);
    }
}
