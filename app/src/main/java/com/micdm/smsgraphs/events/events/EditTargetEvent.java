package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

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
