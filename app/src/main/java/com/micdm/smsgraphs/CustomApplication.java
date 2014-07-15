package com.micdm.smsgraphs;

import android.app.Application;

import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.intents.IntentBasedEventManager;

public class CustomApplication extends Application {

    private DbHelper _dbHelper;
    private EventManager _eventManager;

    public DbHelper getDbHelper() {
        if (_dbHelper == null) {
            _dbHelper = new DbHelper(this);
        }
        return _dbHelper;
    }

    public EventManager getEventManager() {
        if (_eventManager == null) {
            _eventManager = new IntentBasedEventManager(this);
        }
        return _eventManager;
    }
}
