package com.micdm.smsgraphs.events.intent;

import android.content.Context;
import android.content.Intent;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;

public class EventConverter {

    private final Context context;

    public EventConverter(Context context) {
        this.context = context;
    }

    public Intent convert(Event event) {
        Intent intent = new Intent(getIntentAction(event.getType()));
        switch (event.getType()) {
            case PROGRESS_LOAD_MESSAGES:
                convertProgressLoadMessagesEvent(intent, (ProgressLoadMessagesEvent) event);
                break;
        }
        return intent;
    }

    public String getIntentAction(EventType type) {
        return String.format("%s.event.%s", context.getPackageName(), type);
    }

    private void convertProgressLoadMessagesEvent(Intent intent, ProgressLoadMessagesEvent event) {
        intent.putExtra("total", event.getTotal());
        intent.putExtra("current", event.getCurrent());
    }
}
