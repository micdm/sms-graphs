package com.micdm.smsgraphs.data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthOperationList {

    public final Calendar month;
    public final List<Operation> operations;

    public MonthOperationList(Calendar month, List<Operation> operations) {
        this.month = month;
        this.operations = operations;
    }
}
