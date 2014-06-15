package com.micdm.smsgraphs.parser;

import java.math.BigDecimal;

public class Message {

    public static enum Type {
        DEPOSIT,
        WITHDRAWAL,
        PURCHASE,
        TRANSFER
    }

    private final String card;
    private final int year;
    private final int month;
    private final Type type;
    private final String target;
    private final BigDecimal amount;

    public Message(String card, int year, int month, Type type, String target, BigDecimal amount) {
        this.card = card;
        this.year = year;
        this.month = month;
        this.type = type;
        this.target = target;
        this.amount = amount;
    }

    public String getCard() {
        return card;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
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

    public boolean isIncome() {
        return type == Type.DEPOSIT;
    }

    public boolean isOutcome() {
        return type == Type.WITHDRAWAL || type == Type.PURCHASE || type == Type.TRANSFER;
    }
}
