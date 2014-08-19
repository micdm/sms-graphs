package com.micdm.smsgraphs.misc;

import android.util.Log;

public class Logger {

    private static final String TAG = "com.micdm.smsgraphs";

    public static void debug(String message) {
        Log.d(TAG, message);
    }
}
