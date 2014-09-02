package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class RequestLoadCategoriesEvent extends Event {

    public RequestLoadCategoriesEvent() {
        super(EventType.REQUEST_LOAD_CATEGORIES);
    }
}
