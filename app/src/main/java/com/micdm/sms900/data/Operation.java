package com.micdm.sms900.data;

import org.joda.time.DateTime;

public class Operation {

    private final int _id;
    private final Target _target;
    private final DateTime _created;
    private final int _amount;
    private boolean _isIgnored;

    public Operation(int id, Target target, DateTime created, int amount, boolean isIgnored) {
        _id = id;
        _target = target;
        _created = created;
        _amount = amount;
        _isIgnored = isIgnored;
    }

    public int getId() {
        return _id;
    }

    public Target getTarget() {
        return _target;
    }

    public DateTime getCreated() {
        return _created;
    }

    public int getAmount() {
        return _amount;
    }

    public boolean isIgnored() {
        return _isIgnored;
    }

    public void setIgnored(boolean isIgnored) {
        _isIgnored = isIgnored;
    }
}
