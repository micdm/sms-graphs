package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.Operation;

import org.joda.time.DateTime;

public interface OperationHandler {

    public void loadOperations(DateTime date);
    public void setOperationIgnored(Operation operation, boolean isIgnored);
}
