package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;

public class Operation {

    private final int _id;
    private final Target _target;
    private final DateTime _created;
    private final int _amount;

    public Operation(int id, Target target, DateTime created, int amount) {
        _id = id;
        _target = target;
        _created = created;
        _amount = amount;
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
}
