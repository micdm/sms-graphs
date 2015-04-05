package com.micdm.sms900.tests.parser;

import android.test.InstrumentationTestCase;

import com.micdm.sms900.data.Message;
import com.micdm.sms900.messages.MessageParser;

public class MessageParserTest extends InstrumentationTestCase {

    private final static String[] TEXTS = new String[] {
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 р. SOME PLACE 1 Баланс: 1234.56 р.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01р. SOME PLACE 1. Баланс: 1234.56р.",
        "VISA1234 31.01.14 22:50 покупка 2014.01р SOME PLACE 1 Баланс: 1234.56р",
    };

    public void testGetCard() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals("VISA1234", message.getCard());
        }
    }

    public void testGetYear() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(2014, message.getCreated().getYear());
        }
    }

    public void testGetMonth() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(1, message.getCreated().getMonthOfYear());
        }
    }

    public void testGetDay() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(31, message.getCreated().getDayOfMonth());
        }
    }

    public void testGetHour() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(22, message.getCreated().getHourOfDay());
        }
    }

    public void testGetMinute() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(50, message.getCreated().getMinuteOfHour());
        }
    }

    public void testGetSecond() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(0, message.getCreated().getSecondOfMinute());
        }
    }

    public void testGetTarget() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals("SOME PLACE 1", message.getTarget());
        }
    }

    public void testGetAmount() {
        for (String text: TEXTS) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(2014, message.getAmount());
        }
    }
}
