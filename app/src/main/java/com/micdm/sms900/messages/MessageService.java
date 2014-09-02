package com.micdm.sms900.messages;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;

import com.micdm.sms900.CustomApplication;
import com.micdm.sms900.events.EventManager;
import com.micdm.sms900.events.events.FinishLoadMessagesEvent;
import com.micdm.sms900.events.events.ProgressLoadMessagesEvent;
import com.micdm.sms900.events.events.StartLoadMessagesEvent;

import org.joda.time.DateTime;

public class MessageService extends Service {

    private static final String THREAD_NAME = "MessageService";

    private final HandlerThread _thread = new HandlerThread(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND);
    private Handler _handler;

    @Override
    public void onCreate() {
        _thread.start();
        final MessageLoader loader = getLoader();
        _handler = new Handler(_thread.getLooper()) {
            @Override
            public void handleMessage(Message message) {
                loader.load();
            }
        };
    }

    private MessageLoader getLoader() {
        final EventManager manager = ((CustomApplication) getApplication()).getEventManager();
        return new MessageLoader(getApplicationContext(), ((CustomApplication) getApplication()).getDbHelper(), new MessageLoader.OnLoadListener() {
            @Override
            public void onStartLoad() {
                manager.publish(new StartLoadMessagesEvent());
            }
            @Override
            public void onProgress(int total, int current, com.micdm.sms900.data.Message message) {
                DateTime date = (message == null) ? null : message.getCreated();
                manager.publish(new ProgressLoadMessagesEvent(total, current, date));
            }
            @Override
            public void onFinishLoad() {
                manager.publish(new FinishLoadMessagesEvent());
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _handler.sendEmptyMessage(0);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        _thread.quit();
    }
}
