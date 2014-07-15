package com.micdm.smsgraphs.events.intent;

import android.content.Intent;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;
import com.micdm.smsgraphs.events.events.FinishLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.ProgressLoadMessagesEvent;
import com.micdm.smsgraphs.events.events.StartLoadMessagesEvent;

public class IntentConverter {

    public Event convert(Intent intent) {
        switch (getEventTypeFromIntent(intent)) {
            case START_LOAD_MESSAGES:
                return new StartLoadMessagesEvent();
            case PROGRESS_LOAD_MESSAGES:
                return new ProgressLoadMessagesEvent(intent.getIntExtra("total", -1), intent.getIntExtra("current", -1));
            case FINISH_LOAD_MESSAGES:
                return new FinishLoadMessagesEvent();
            default:
                throw new RuntimeException("unknown event type");
        }
    }

    private EventType getEventTypeFromIntent(Intent intent) {
        String action = intent.getAction();
        String[] parts = action.split("\\.");
        String typeName = parts[parts.length - 1];
        for (EventType type: EventType.values()) {
            if (type.toString().equals(typeName)) {
                return type;
            }
        }
        throw new RuntimeException("unknown event type");
    }
}
