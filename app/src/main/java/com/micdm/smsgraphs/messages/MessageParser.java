package com.micdm.smsgraphs.messages;

import com.micdm.smsgraphs.data.Message;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: оплата мобильного банка за ... на сумму ...
public class MessageParser {

    private static final String PATTERN_V1 = "^(\\w+\\d+): (\\d{2})\\.(\\d{2})\\.(\\d{2}) (\\d{2}):(\\d{2}) (.+?) на сумму ([\\d\\.]+) руб\\. (.+?) выполнена успешно";
    private static final String PATTERN_V2 = "^(\\w+\\d+): (\\d{2})\\.(\\d{2})\\.(\\d{2}) (\\d{2}):(\\d{2}) (.+?) на сумму ([\\d\\.]+) р\\. (.+?) Баланс";
    private static final String PATTERN_V3 = "^(\\w+\\d+): (\\d{2})\\.(\\d{2})\\.(\\d{2}) (\\d{2}):(\\d{2}) (.+?) на сумму ([\\d\\.]+)р\\. (.+?)\\. Баланс";

    private static final int GROUP_CARD = 1;
    private static final int GROUP_DAY = 2;
    private static final int GROUP_MONTH = 3;
    private static final int GROUP_YEAR = 4;
    private static final int GROUP_HOUR = 5;
    private static final int GROUP_MINUTE = 6;
    private static final int GROUP_TYPE = 7;
    private static final int GROUP_AMOUNT = 8;
    private static final int GROUP_TARGET = 9;

    private final List<Pattern> _patterns = new ArrayList<Pattern>();

    public MessageParser() {
        _patterns.add(Pattern.compile(PATTERN_V1));
        _patterns.add(Pattern.compile(PATTERN_V2));
        _patterns.add(Pattern.compile(PATTERN_V3));
    }

    public Message parse(String message) {
        for (Pattern pattern: _patterns) {
            Message result = parse(message, pattern);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private Message parse(String message, Pattern pattern) {
        Matcher m = pattern.matcher(message);
        return (m.find() && isOperation(m)) ? new Message(getCard(m), getCreated(m), getTarget(m), getAmount(m)) : null;
    }

    private boolean isOperation(Matcher m) {
        String type = m.group(GROUP_TYPE);
        return type.equals("оплата услуг") || type.equals("покупка") || type.equals("оплата обслуживания банковской карты");
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

    private String getTarget(Matcher m) {
        return m.group(GROUP_TARGET);
    }

    private int getAmount(Matcher m) {
        return (int) Math.round(Double.valueOf(m.group(GROUP_AMOUNT)));
    }
}
