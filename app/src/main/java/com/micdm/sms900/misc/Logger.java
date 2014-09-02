package com.micdm.sms900.misc;

import android.util.Log;

public class Logger {

    private static final String TAG = "com.micdm.sms900";

    public static void debug(String message) {
        Log.d(TAG, message);
    }
}
