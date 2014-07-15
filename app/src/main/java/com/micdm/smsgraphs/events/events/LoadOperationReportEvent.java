package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.OperationReport;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

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
