package com.micdm.smsgraphs.messages;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;

import com.micdm.smsgraphs.CustomApplication;
import com.micdm.smsgraphs.events.EventManager;
import com.micdm.smsgraphs.events.events.FinishLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.StartLoadMessagesEvent;

public class MessageService extends Service {

    private static final String THREAD_NAME = "MessageService";

    private final HandlerThread thread = new HandlerThread(THREAD_NAME, Process.THREAD_PRIORITY_BACKGROUND);
    private Handler handler;

    @Override
    public void onCreate() {
        thread.start();
        final MessageLoader loader = getLoader();
        handler = new Handler(thread.getLooper()) {
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
            public void onProgress(int total, int current) {
                manager.publish(new ProgressLoadMessagesEvent(total, current));
            }
            @Override
            public void onFinishLoad() {
                manager.publish(new FinishLoadMessagesEvent());
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.sendEmptyMessage(0);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        thread.quit();
    }
}
