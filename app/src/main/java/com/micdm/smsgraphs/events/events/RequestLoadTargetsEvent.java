package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestLoadTargetsEvent extends Event {

    public RequestLoadTargetsEvent() {
        super(EventType.REQUEST_LOAD_TARGETS);
    }
}
