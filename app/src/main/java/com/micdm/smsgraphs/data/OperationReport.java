package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class OperationReport {

    public final DateTime first;
    public final DateTime last;

    public OperationReport(DateTime first, DateTime last) {
        this.first = first;
        this.last = last;
    }

    public int getMonthCount() {
        if (first == null || last == null) {
            return 0;
        }
        DateTime begin = first.withDayOfMonth(1).withMillisOfDay(0);
        DateTime end = last.plusMonths(1).withDayOfMonth(1).withMillisOfDay(0);
        return new Period(begin, end).getMonths();
    }
}
