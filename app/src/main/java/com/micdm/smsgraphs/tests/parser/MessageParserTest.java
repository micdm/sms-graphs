package com.micdm.smsgraphs.tests.parser;

import android.test.InstrumentationTestCase;

import com.micdm.smsgraphs.messages.Message;
import com.micdm.smsgraphs.messages.MessageParser;

import java.math.BigDecimal;

public class MessageParserTest extends InstrumentationTestCase {

    private final String[] texts = new String[] {
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 выдача наличных на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 22:50 операция списания на сумму 2014.01 руб. с комиссией 20.14 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 оплата Мобильного банка за 31/01/2014-28/02/2014 на сумму 60.00 руб. выполнена успешно. Доступно: 1234.56 руб."
    };

    public void testGetCard() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals("VISA1234", message.getCard());
        }
    }

    public void testGetYear() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(114, message.getCreated().getYear());
        }
    }

    public void testGetMonth() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(0, message.getCreated().getMonth());
        }
    }

    public void testGetDay() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(31, message.getCreated().getDate());
        }
    }

    public void testGetHour() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(22, message.getCreated().getHours());
        }
    }

    public void testGetMinute() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(50, message.getCreated().getMinutes());
        }
    }

    public void testGetTarget() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals("SOME PLACE 1", message.getTarget());
        }
    }

    public void testGetAmount() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(new BigDecimal("2014.01"), message.getAmount());
        }
    }
}
