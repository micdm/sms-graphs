package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class FinishLoadMessagesEvent extends Event {

    public FinishLoadMessagesEvent() {
        super(EventType.FINISH_LOAD_MESSAGES);
    }
}
