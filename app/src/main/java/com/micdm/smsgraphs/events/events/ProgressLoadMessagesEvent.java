package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class ProgressLoadMessagesEvent extends Event {

    private final int total;
    private final int current;

    public ProgressLoadMessagesEvent(int total, int current) {
        super(EventType.PROGRESS_LOAD_MESSAGES);
        this.total = total;
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrent() {
        return current;
    }
}
