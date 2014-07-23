package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

import org.joda.time.DateTime;

public class RequestLoadOperationsEvent extends Event {

    private final DateTime _date;

    public RequestLoadOperationsEvent(DateTime date) {
        super(EventType.REQUEST_LOAD_OPERATIONS);
        _date = date;
    }

    public DateTime getDate() {
        return _date;
    }
}
