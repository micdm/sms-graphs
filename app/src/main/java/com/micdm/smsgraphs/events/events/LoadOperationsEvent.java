package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.MonthOperationList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class LoadOperationsEvent extends Event {

    private final MonthOperationList _operations;

    public LoadOperationsEvent(MonthOperationList operations) {
        super(EventType.LOAD_OPERATIONS);
        _operations = operations;
    }

    public MonthOperationList getOperations() {
        return _operations;
    }
}
