package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestNextMonthOperationsEvent extends Event {

    public RequestNextMonthOperationsEvent() {
        super(EventType.REQUEST_NEXT_MONTH_OPERATIONS);
    }
}
