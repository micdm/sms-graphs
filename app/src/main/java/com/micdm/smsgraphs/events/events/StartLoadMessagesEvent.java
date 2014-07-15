package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class StartLoadMessagesEvent extends Event {

    public StartLoadMessagesEvent() {
        super(EventType.START_LOAD_MESSAGES);
    }
}
