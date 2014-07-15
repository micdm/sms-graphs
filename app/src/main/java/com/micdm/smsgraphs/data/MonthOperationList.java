package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;

import java.util.List;

public class MonthOperationList {

    private final DateTime _month;
    private final List<Operation> _operations;

    public MonthOperationList(DateTime month, List<Operation> operations) {
        _month = month;
        _operations = operations;
    }

    public DateTime getMonth() {
        return _month;
    }

    public List<Operation> getOperations() {
        return _operations;
    }
}
