package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class ProgressLoadMessagesEvent extends Event {

    private final int _total;
    private final int _current;

    public ProgressLoadMessagesEvent(int total, int current) {
        super(EventType.PROGRESS_LOAD_MESSAGES);
        _total = total;
        _current = current;
    }

    public int getTotal() {
        return _total;
    }

    public int getCurrent() {
        return _current;
    }
}
