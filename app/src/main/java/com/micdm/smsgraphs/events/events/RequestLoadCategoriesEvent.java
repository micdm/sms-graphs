package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestLoadCategoriesEvent extends Event {

    public RequestLoadCategoriesEvent() {
        super(EventType.REQUEST_LOAD_CATEGORIES);
    }
}
