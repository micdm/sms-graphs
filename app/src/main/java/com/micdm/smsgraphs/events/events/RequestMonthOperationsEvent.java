package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

import org.joda.time.DateTime;

public class RequestMonthOperationsEvent extends Event {

    private final DateTime _date;

    public RequestMonthOperationsEvent(DateTime date) {
        super(EventType.REQUEST_MONTH_OPERATIONS);
        _date = date;
    }

    public DateTime getDate() {
        return _date;
    }
}
