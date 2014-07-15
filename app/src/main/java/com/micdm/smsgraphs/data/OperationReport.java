package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;
import org.joda.time.Period;

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

    public int getMonthCount() {
        if (_first == null || _last == null) {
            return 0;
        }
        DateTime begin = _first.withDayOfMonth(1).withMillisOfDay(0);
        DateTime end = _last.plusMonths(1).withDayOfMonth(1).withMillisOfDay(0);
        return new Period(begin, end).getMonths();
    }
}
