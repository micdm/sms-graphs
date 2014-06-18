package com.micdm.smsgraphs.data;

import java.util.Calendar;
import java.util.Date;

public class OperationReport {

    public final Calendar first;
    public final Calendar last;

    public OperationReport(Calendar first, Calendar last) {
        this.first = first;
        this.last = last;
    }
}
