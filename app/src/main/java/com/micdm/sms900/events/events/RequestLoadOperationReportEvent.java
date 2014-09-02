package com.micdm.sms900.events.events;

import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class RequestLoadOperationReportEvent extends Event {

    public RequestLoadOperationReportEvent() {
        super(EventType.REQUEST_LOAD_OPERATION_REPORT);
    }
}
