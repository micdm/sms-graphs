package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestLoadOperationReportEvent extends Event {

    public RequestLoadOperationReportEvent() {
        super(EventType.REQUEST_LOAD_OPERATION_REPORT);
    }
}
