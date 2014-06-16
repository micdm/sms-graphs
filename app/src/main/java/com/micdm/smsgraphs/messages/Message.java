package com.micdm.smsgraphs.messages;

import java.math.BigDecimal;
import java.util.Date;

public class Message {

    public static enum Type {
        WITHDRAWAL,
        PURCHASE,
        TRANSFER
    }

    private final String card;
    private final Date created;
    private final Type type;
    private final String target;
    private final BigDecimal amount;

    public Message(String card, Date created, Type type, String target, BigDecimal amount) {
        this.card = card;
        this.created = created;
        this.type = type;
        this.target = target;
        this.amount = amount;
    }

    public String getCard() {
        return card;
    }

    public Date getCreated() {
        return created;
    }

    public Type getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
