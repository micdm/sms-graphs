package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestPreviousMonthOperationsEvent extends Event {

    public RequestPreviousMonthOperationsEvent() {
        super(EventType.REQUEST_PREVIOUS_MONTH_OPERATIONS);
    }
}
