package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.OutcomeTarget;
import com.micdm.utils.events.EventListener;

import java.util.List;

public interface TargetHandler {

    public static interface OnLoadTargetsListener extends EventListener {
        public void onLoadTargets(List<OutcomeTarget> targets);
    }

    public void addOnLoadTargetsListener(OnLoadTargetsListener listener);
    public void removeOnLoadTargetsListener(OnLoadTargetsListener listener);
}
