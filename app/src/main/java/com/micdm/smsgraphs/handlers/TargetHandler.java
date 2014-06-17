package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.TargetList;
import com.micdm.utils.events.EventListener;

public interface TargetHandler {

    public static interface OnLoadTargetsListener extends EventListener {
        public void onLoadTargets(TargetList targets);
    }

    public void updateWithNoCategoryCount(int count);
    public void addOnLoadTargetsListener(OnLoadTargetsListener listener);
    public void removeOnLoadTargetsListener(OnLoadTargetsListener listener);
}
