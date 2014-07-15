package com.micdm.smsgraphs.events.events;

import com.micdm.smsgraphs.events.Event;
import com.micdm.smsgraphs.events.EventType;

public class EditTargetEvent extends Event {

    public EditTargetEvent() {
        super(EventType.EDIT_TARGET);
    }
}
