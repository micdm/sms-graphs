package com.micdm.sms900.events.events;

import com.micdm.sms900.data.Target;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class EditTargetEvent extends Event {

    private final Target _target;
    private final boolean _needEditNext;

    public EditTargetEvent(Target target, boolean needEditNext) {
        super(EventType.EDIT_TARGET);
        _target = target;
        _needEditNext = needEditNext;
    }

    public Target getTarget() {
        return _target;
    }

    public boolean needEditNext() {
        return _needEditNext;
    }
}
