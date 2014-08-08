package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

import org.joda.time.DateTime;

public class ProgressLoadMessagesEvent extends Event {

    private final int _total;
    private final int _current;
    private final DateTime _date;

    public ProgressLoadMessagesEvent(int total, int current, DateTime date) {
        super(EventType.PROGRESS_LOAD_MESSAGES);
        _total = total;
        _current = current;
        _date = date;
    }

    public int getTotal() {
        return _total;
    }

    public int getCurrent() {
        return _current;
    }

    public DateTime getDate() {
        return _date;
    }
}
