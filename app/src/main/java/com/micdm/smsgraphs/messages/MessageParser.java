package com.micdm.smsgraphs.messages;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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

    public static Message parse(String message) {
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

    private static String getCard(Matcher m) {
        return m.group(GROUP_CARD);
    }

    private static int getYear(Matcher m) {
        return 2000 + Integer.valueOf(m.group(GROUP_YEAR));
    }

    private static int getMonth(Matcher m) {
        return Integer.valueOf(m.group(GROUP_MONTH));
    }

    private static int getDay(Matcher m) {
        return Integer.valueOf(m.group(GROUP_DAY));
    }

    private static int getHour(Matcher m) {
        return Integer.valueOf(m.group(GROUP_HOUR));
    }

    private static int getMinute(Matcher m) {
        return Integer.valueOf(m.group(GROUP_MINUTE));
    }

    private static Calendar getCreated(Matcher m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getYear(m));
        calendar.set(Calendar.MONTH, getMonth(m) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, getDay(m));
        calendar.set(Calendar.HOUR_OF_DAY, getHour(m));
        calendar.set(Calendar.MINUTE, getMinute(m));
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private static boolean isOperation(Matcher m) {
        String type = m.group(GROUP_TYPE);
        return type.equals("оплата услуг") || type.equals("покупка") || type.equals("оплата обслуживания банковской карты");
    }

    private static String getTarget(Matcher m) {
        return m.group(GROUP_TARGET);
    }

    private static BigDecimal getAmount(Matcher m) {
        return new BigDecimal(m.group(GROUP_AMOUNT));
    }
}
