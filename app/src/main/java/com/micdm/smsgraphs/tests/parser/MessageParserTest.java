package com.micdm.smsgraphs.tests.parser;

import android.test.InstrumentationTestCase;

import com.micdm.smsgraphs.parser.Message;
import com.micdm.smsgraphs.parser.MessageParser;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParserTest extends InstrumentationTestCase {

    private final String[] texts = new String[] {
        "VISA1234: 31.01.14 22:50 оплата услуг на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 покупка на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 выдача наличных на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        "VISA1234: 31.01.14 22:50 операция зачисления на сумму 2014.01 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 22:50 операция списания на сумму 2014.01 руб. с комиссией 20.14 руб. SOME PLACE 1 выполнена успешно. Доступно: 1234.56 руб.",
        //"VISA1234: 31.01.14 оплата Мобильного банка за 31/01/2014-28/02/2014 на сумму 60.00 руб. выполнена успешно. Доступно: 1234.56 руб."
    };

    public void testFoo() {
        Pattern pattern = Pattern.compile("^привет");
        Matcher matcher = pattern.matcher("привет");
        assertTrue(matcher.find());
        assertEquals("привет", matcher.group(0));
    }

    public void testGetCard() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals("VISA1234", message.getCard());
        }
    }

    public void testGetYear() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(2014, message.getYear());
        }
    }

    public void testGetMonth() {
        for (String text: texts) {
            Message message = MessageParser.parse(text);
            assertEquals(0, message.getMonth());
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

    public void testIsIncome() {
        assertFalse(MessageParser.parse(texts[0]).isIncome());
        assertFalse(MessageParser.parse(texts[1]).isIncome());
        assertFalse(MessageParser.parse(texts[2]).isIncome());
        assertTrue(MessageParser.parse(texts[3]).isIncome());
        //assertFalse(MessageParser.parse(texts[4]).isIncome());
    }

    public void testIsOutcome() {
        assertTrue(MessageParser.parse(texts[0]).isOutcome());
        assertTrue(MessageParser.parse(texts[1]).isOutcome());
        assertTrue(MessageParser.parse(texts[2]).isOutcome());
        assertFalse(MessageParser.parse(texts[3]).isOutcome());
        //assertTrue(MessageParser.parse(texts[4]).isOutcome());
    }
}
