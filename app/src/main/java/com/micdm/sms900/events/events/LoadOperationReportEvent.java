package com.micdm.sms900.events.events;

import com.micdm.sms900.data.OperationReport;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class LoadOperationReportEvent extends Event {

    private final OperationReport _report;

    public LoadOperationReportEvent(OperationReport report) {
        super(EventType.LOAD_OPERATION_REPORT);
        _report = report;
    }

    public OperationReport getReport() {
        return _report;
    }
}
