package com.micdm.smsgraphs.parser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {

    public static Message parse(String message) {
        Pattern pattern = Pattern.compile("^(VISA\\d+): \\d{2}\\.(\\d{2})\\.(\\d{2}) \\d{2}:\\d{2} (.+?) на сумму ([\\d\\.]+) руб\\. (.+?) выполнена успешно");
        Matcher m = pattern.matcher(message);
        if (!m.find()) {
            return null;
        }
        return new Message(getCard(m), getYear(m), getMonth(m), getType(m), getTarget(m), getAmount(m));
    }

    private static String getCard(Matcher m) {
        return m.group(1);
    }

    private static int getYear(Matcher m) {
        return 2000 + Integer.valueOf(m.group(3));
    }

    private static int getMonth(Matcher m) {
        return Integer.valueOf(m.group(2)) - 1;
    }

    private static Message.Type getType(Matcher m) {
        String type = m.group(4);
        if (type.equals("операция зачисления")) {
            return Message.Type.DEPOSIT;
        }
        if (type.equals("выдача наличных")) {
            return Message.Type.WITHDRAWAL;
        }
        if (type.equals("оплата услуг") || type.equals("покупка") || type.equals("оплата обслуживания банковской карты")) {
            return Message.Type.PURCHASE;
        }
        if (type.equals("операция списания")) {
            return Message.Type.TRANSFER;
        }
        throw new RuntimeException(String.format("unknown message type \"%s\"", type));
    }

    private static String getTarget(Matcher m) {
        return m.group(6);
    }

    private static BigDecimal getAmount(Matcher m) {
        return new BigDecimal(m.group(5));
    }
}
