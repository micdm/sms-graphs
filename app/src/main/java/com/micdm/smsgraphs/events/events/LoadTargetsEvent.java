package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.data.TargetList;
import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class LoadTargetsEvent extends Event {

    private final TargetList targets;

    public LoadTargetsEvent(TargetList targets) {
        super(EventType.LOAD_TARGETS);
        this.targets = targets;
    }

    public TargetList getTargets() {
        return targets;
    }
}
