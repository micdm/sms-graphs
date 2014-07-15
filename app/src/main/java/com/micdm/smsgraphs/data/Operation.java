package com.micdm.smsgraphs.data;

public class Operation {

    private final Target _target;
    private final int _amount;

    public Operation(Target target, int amount) {
        _target = target;
        _amount = amount;
    }

    public Target getTarget() {
        return _target;
    }

    public int getAmount() {
        return _amount;
    }
}
