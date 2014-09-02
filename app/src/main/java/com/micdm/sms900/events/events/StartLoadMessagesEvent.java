package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class StartLoadMessagesEvent extends Event {

    public StartLoadMessagesEvent() {
        super(EventType.START_LOAD_MESSAGES);
    }
}
