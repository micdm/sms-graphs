package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

import org.joda.time.DateTime;

public class RequestSelectMonthEvent extends Event {

    private final DateTime _current;

    public RequestSelectMonthEvent(DateTime current) {
        super(EventType.REQUEST_SELECT_MONTH);
        _current = current;
    }

    public DateTime getCurrent() {
        return _current;
    }
}
