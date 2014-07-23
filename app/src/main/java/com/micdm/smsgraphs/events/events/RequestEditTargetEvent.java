package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class RequestEditTargetEvent extends Event {

    private final Target _target;

    public RequestEditTargetEvent(Target target) {
        super(EventType.REQUEST_EDIT_TARGET);
        _target = target;
    }

    public Target getTarget() {
        return _target;
    }
}
