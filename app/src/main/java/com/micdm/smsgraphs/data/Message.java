package com.micdm.smsgraphs.data;

import java.math.BigDecimal;
import java.util.Calendar;

public class Message {

    private final String card;
    private final Calendar created;
    private final String target;
    private final BigDecimal amount;

    public Message(String card, Calendar created, String target, BigDecimal amount) {
        this.card = card;
        this.created = created;
        this.target = target;
        this.amount = amount;
    }

    public String getCard() {
        return card;
    }

    public Calendar getCreated() {
        return created;
    }

    public String getTarget() {
        return target;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
