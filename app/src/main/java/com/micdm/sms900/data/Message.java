package com.micdm.sms900.data;

import org.joda.time.DateTime;

public class Message {

    private final String _card;
    private final DateTime _created;
    private final String _target;
    private final int _amount;

    public Message(String card, DateTime created, String target, int amount) {
        _card = card;
        _created = created;
        _target = target;
        _amount = amount;
    }

    public String getCard() {
        return _card;
    }

    public DateTime getCreated() {
        return _created;
    }

    public String getTarget() {
        return _target;
    }

    public int getAmount() {
        return _amount;
    }
}
