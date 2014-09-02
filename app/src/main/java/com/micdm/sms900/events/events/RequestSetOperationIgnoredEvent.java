package com.micdm.sms900.events.events;

import com.micdm.sms900.data.Operation;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class RequestSetOperationIgnoredEvent extends Event {

    private final Operation _operation;
    private final boolean _needIgnore;

    public RequestSetOperationIgnoredEvent(Operation operation, boolean needIgnore) {
        super(EventType.REQUEST_SET_OPERATION_IGNORED);
        _operation = operation;
        _needIgnore = needIgnore;
    }

    public Operation getOperation() {
        return _operation;
    }

    public boolean needIgnore() {
        return _needIgnore;
    }
}
