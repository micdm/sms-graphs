package com.micdm.smsgraphs.tests.parser;

import android.test.InstrumentationTestCase;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.messages.MessageParser;

public class MessageParserTest extends InstrumentationTestCase {

    private final String[] texts = new String[] {
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 р. SOME PLACE 1 Баланс: 1234.56 р."
    };

    public void testGetCard() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals("VISA1234", message.card);
        }
    }

    public void testGetYear() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(2014, message.created.getYear());
        }
    }

    public void testGetMonth() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(1, message.created.getMonthOfYear());
        }
    }

    public void testGetDay() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(31, message.created.getDayOfMonth());
        }
    }

    public void testGetHour() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(22, message.created.getHourOfDay());
        }
    }

    public void testGetMinute() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(50, message.created.getMinuteOfHour());
        }
    }

    public void testGetSecond() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(0, message.created.getSecondOfMinute());
        }
    }

    public void testGetTarget() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals("SOME PLACE 1", message.target);
        }
    }

    public void testGetAmount() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(2014, message.amount);
        }
    }
}
