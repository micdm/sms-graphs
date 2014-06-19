package com.micdm.smsgraphs.data;

import java.util.Calendar;

public class Operation {

    public final int id;
    public final Target target;
    public final Calendar created;
    public final int amount;

    public Operation(int id, Target target, Calendar created, int amount) {
        this.id = id;
        this.target = target;
        this.created = created;
        this.amount = amount;
    }
}
