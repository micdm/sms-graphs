package com.micdm.smsgraphs.handlers;

import com.micdm.smsgraphs.data.Target;
import com.micdm.smsgraphs.data.TargetList;
import com.micdm.utils.events.EventListener;

public interface TargetHandler {

    public static interface OnLoadTargetsListener extends EventListener {
        public void onLoadTargets(TargetList targets);
    }

    public static interface OnStartEditTargetListener extends EventListener {
        public void onStartEditTarget(Target target);
    }

    public static interface OnEditTargetListener extends EventListener {
        public void onEditTarget();
    }

    public void startEditTarget(Target target);
    public void finishEditTarget(Target target, boolean editNext);
    public void addOnLoadTargetsListener(OnLoadTargetsListener listener);
    public void removeOnLoadTargetsListener(OnLoadTargetsListener listener);
    public void addOnStartEditTargetListener(OnStartEditTargetListener listener);
    public void removeOnStartEditTargetListener(OnStartEditTargetListener listener);
    public void addOnEditTargetListener(OnEditTargetListener listener);
    public void removeOnEditTargetListener(OnEditTargetListener listener);
}
