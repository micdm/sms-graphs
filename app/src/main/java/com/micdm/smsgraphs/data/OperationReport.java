package com.micdm.smsgraphs.data;

import java.util.Calendar;

public class OperationReport {

    public final Calendar first;
    public final Calendar last;

    public OperationReport(Calendar first, Calendar last) {
        this.first = first;
        this.last = last;
    }
}
