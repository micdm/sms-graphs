package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

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
