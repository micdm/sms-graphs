package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.Operation;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

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
