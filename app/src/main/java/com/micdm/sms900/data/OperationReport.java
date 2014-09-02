package com.micdm.sms900.data;

import org.joda.time.DateTime;

public class OperationReport {

    private final DateTime _first;
    private final DateTime _last;

    public OperationReport(DateTime first, DateTime last) {
        _first = first;
        _last = last;
    }

    public DateTime getFirst() {
        return _first;
    }

    public DateTime getLast() {
        return _last;
    }
}
