package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

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
