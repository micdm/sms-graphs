package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class LoadOperationsEvent extends Event {

    private final MonthOperationList operations;

    public LoadOperationsEvent(MonthOperationList operations) {
        super(EventType.LOAD_OPERATIONS);
        this.operations = operations;
    }

    public MonthOperationList getOperations() {
        return operations;
    }
}
