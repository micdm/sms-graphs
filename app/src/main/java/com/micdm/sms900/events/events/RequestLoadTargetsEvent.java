package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class RequestLoadTargetsEvent extends Event {

    public RequestLoadTargetsEvent() {
        super(EventType.REQUEST_LOAD_TARGETS);
    }
}
