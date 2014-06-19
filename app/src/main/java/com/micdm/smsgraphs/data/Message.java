package com.micdm.smsgraphs.data;

import java.util.Calendar;

public class Message {

    public final String card;
    public final Calendar created;
    public final String target;
    public final int amount;

    public Message(String card, Calendar created, String target, int amount) {
        this.card = card;
        this.created = created;
        this.target = target;
        this.amount = amount;
    }
}
