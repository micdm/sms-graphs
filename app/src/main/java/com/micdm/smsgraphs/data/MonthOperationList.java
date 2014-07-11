package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;

import java.util.List;

public class MonthOperationList {

    public final DateTime month;
    public final List<Operation> operations;

    public MonthOperationList(DateTime month, List<Operation> operations) {
        this.month = month;
        this.operations = operations;
    }
}
