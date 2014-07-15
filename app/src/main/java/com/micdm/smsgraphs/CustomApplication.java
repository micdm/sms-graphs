package com.micdm.smsgraphs;

import android.app.Application;

import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.intent.IntentBasedEventManager;

public class CustomApplication extends Application {

    private DbHelper dbHelper;
    private EventManager eventManager;

    public DbHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = new DbHelper(this);
        }
        return dbHelper;
    }

    public EventManager getEventManager() {
        if (eventManager == null) {
            eventManager = new IntentBasedEventManager(this);
        }
        return eventManager;
    }
}
