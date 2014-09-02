package com.micdm.sms900.events.events;

import com.micdm.sms900.data.Target;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

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
