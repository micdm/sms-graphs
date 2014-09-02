package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

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
