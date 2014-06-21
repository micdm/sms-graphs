package com.micdm.smsgraphs.tests.parser;

import android.test.InstrumentationTestCase;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.messages.MessageParser;

import java.util.Calendar;

public class MessageParserTest extends InstrumentationTestCase {

    private final String[] texts = new String[] {
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 22:50 выдача наличных на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 22:50 операция списания на сумму 2014.01 руб. с комиссией 20.14 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 оплата Мобильного банка за 31/01/2014-28/02/2014 на сумму 60.00 руб. выполнена успешно. Доступно: 1234.56 руб."
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
            assertEquals(2014, message.created.get(Calendar.YEAR));
        }
    }

    public void testGetMonth() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(0, message.created.get(Calendar.MONTH));
        }
    }

    public void testGetDay() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(31, message.created.get(Calendar.DAY_OF_MONTH));
        }
    }

    public void testGetHour() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(22, message.created.get(Calendar.HOUR_OF_DAY));
        }
    }

    public void testGetMinute() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(50, message.created.get(Calendar.MINUTE));
        }
    }

    public void testGetSecond() {
        for (String text: texts) {
            Message message = (new MessageParser()).parse(text);
            assertEquals(0, message.created.get(Calendar.SECOND));
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
