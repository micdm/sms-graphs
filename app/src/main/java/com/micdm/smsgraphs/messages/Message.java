package com.micdm.smsgraphs.messages;

import java.math.BigDecimal;
import java.util.Date;

public class Message {

    private final String card;
    private final Date created;
    private final String target;
    private final BigDecimal amount;

    public Message(String card, Date created, String target, BigDecimal amount) {
        this.card = card;
        this.created = created;
        this.target = target;
        this.amount = amount;
    }

    public String getCard() {
        return card;
    }

    public Date getCreated() {
        return created;
    }

    public String getTarget() {
        return target;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
