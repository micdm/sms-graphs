package com.micdm.smsgraphs.messages;

import com.micdm.smsgraphs.data.Message;

import org.joda.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {

    private static final int GROUP_CARD = 1;
    private static final int GROUP_DAY = 2;
    private static final int GROUP_MONTH = 3;
    private static final int GROUP_YEAR = 4;
    private static final int GROUP_HOUR = 5;
    private static final int GROUP_MINUTE = 6;
    private static final int GROUP_TYPE = 7;
    private static final int GROUP_AMOUNT = 8;
    private static final int GROUP_TARGET = 9;

    public Message parse(String message) {
        Pattern pattern = Pattern.compile("^(VISA\\d+): (\\d{2})\\.(\\d{2})\\.(\\d{2}) (\\d{2}):(\\d{2}) (.+?) на сумму ([\\d\\.]+) руб\\. (.+?) выполнена успешно");
        Matcher m = pattern.matcher(message);
        if (!m.find()) {
            return null;
        }
        if (isOperation(m)) {
            return new Message(getCard(m), getCreated(m), getTarget(m), getAmount(m));
        }
        return null;
    }

    private String getCard(Matcher m) {
        return m.group(GROUP_CARD);
    }

    private int getYear(Matcher m) {
        return 2000 + Integer.valueOf(m.group(GROUP_YEAR));
    }

    private int getMonth(Matcher m) {
        return Integer.valueOf(m.group(GROUP_MONTH));
    }

    private int getDay(Matcher m) {
        return Integer.valueOf(m.group(GROUP_DAY));
    }

    private int getHour(Matcher m) {
        return Integer.valueOf(m.group(GROUP_HOUR));
    }

    private int getMinute(Matcher m) {
        return Integer.valueOf(m.group(GROUP_MINUTE));
    }

    private DateTime getCreated(Matcher m) {
        return new DateTime(getYear(m), getMonth(m), getDay(m), getHour(m), getMinute(m), 0);
    }

    private boolean isOperation(Matcher m) {
        String type = m.group(GROUP_TYPE);
        return type.equals("оплата услуг") || type.equals("покупка") || type.equals("оплата обслуживания банковской карты");
    }

    private String getTarget(Matcher m) {
        return m.group(GROUP_TARGET);
    }

    private int getAmount(Matcher m) {
        return (int) Math.round(Double.valueOf(m.group(GROUP_AMOUNT)));
    }
}
