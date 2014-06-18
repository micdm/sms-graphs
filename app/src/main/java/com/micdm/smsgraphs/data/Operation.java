package com.micdm.smsgraphs.data;

import java.math.BigDecimal;
import java.util.Calendar;

public class Operation {

    public final int id;
    public final Target target;
    public final Calendar created;
    public final BigDecimal amount;

    public Operation(int id, Target target, Calendar created, BigDecimal amount) {
        this.id = id;
        this.target = target;
        this.created = created;
        this.amount = amount;
    }
}
