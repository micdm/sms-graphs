package com.micdm.sms900;

import android.app.Application;

import com.micdm.sms900.db.DbHelper;
import com.micdm.sms900.events.EventManager;
import com.micdm.sms900.events.intents.IntentBasedEventManager;

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
