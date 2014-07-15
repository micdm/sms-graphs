package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.Target;

public interface TargetHandler {

    public void loadTargets();
    public void requestEditTarget(Target target);
    public void editTarget(Target target, boolean editNext);
}
