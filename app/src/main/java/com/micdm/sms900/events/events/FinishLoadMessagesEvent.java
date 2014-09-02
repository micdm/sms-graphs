package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class FinishLoadMessagesEvent extends Event {

    public FinishLoadMessagesEvent() {
        super(EventType.FINISH_LOAD_MESSAGES);
    }
}
