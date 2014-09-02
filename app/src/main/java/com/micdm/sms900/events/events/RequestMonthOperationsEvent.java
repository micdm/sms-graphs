package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

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
