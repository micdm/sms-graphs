package com.micdm.smsgraphs.data;

public class Operation {

    public final Target target;
    public final int amount;

    public Operation(Target target, int amount) {
        this.target = target;
        this.amount = amount;
    }
}
