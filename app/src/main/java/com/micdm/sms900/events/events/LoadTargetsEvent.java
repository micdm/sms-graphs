package com.micdm.sms900.events.events;

import com.micdm.sms900.data.TargetList;
import com.micdm.sms900.events.Event;
import com.micdm.sms900.events.EventType;

public class LoadTargetsEvent extends Event {

    private final TargetList _targets;

    public LoadTargetsEvent(TargetList targets) {
        super(EventType.LOAD_TARGETS);
        _targets = targets;
    }

    public TargetList getTargets() {
        return _targets;
    }
}
