package com.micdm.sms900.events.events;

import com.micdm.sms900.data.MonthOperationList;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

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
