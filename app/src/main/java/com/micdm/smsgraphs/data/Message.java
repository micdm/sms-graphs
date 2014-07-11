package com.micdm.smsgraphs.data;

import org.joda.time.DateTime;

public class Message {

    public final String card;
    public final DateTime created;
    public final String target;
    public final int amount;

    public Message(String card, DateTime created, String target, int amount) {
        this.card = card;
        this.created = created;
        this.target = target;
        this.amount = amount;
    }
}
