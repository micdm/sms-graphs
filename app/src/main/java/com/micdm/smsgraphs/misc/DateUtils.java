package com.micdm.smsgraphs.misc;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    private static final DateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat humanFormat = new SimpleDateFormat("d MMMM");
    private static final DateFormat humanAnotherYearFormat = new SimpleDateFormat("d MMMM yyyy");
    private static final DateFormat humanMonthFormat = new SimpleDateFormat("LLLL");
    private static final DateFormat humanAnotherYearMonthFormat = new SimpleDateFormat("LLLL yyyy");

    public synchronized static String formatForDb(DateTime date) {
        return dbFormat.format(date.toDate());
    }

    public synchronized static DateTime parseForDb(String string) {
        try {
            return new DateTime(dbFormat.parse(string).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(String.format("cannot parse date %s", string));
        }
    }

    public synchronized static String formatForHuman(DateTime date) {
        DateTime now = new DateTime();
        if (date.getYear() == now.getYear()) {
            return humanFormat.format(date.toDate());
        }
        return humanAnotherYearFormat.format(date.toDate());
    }

    public synchronized static String formatMonthForHuman(DateTime date) {
        DateTime now = new DateTime();
        if (date.getYear() == now.getYear()) {
            return humanMonthFormat.format(date.toDate());
        }
        return humanAnotherYearMonthFormat.format(date.toDate());
    }
}
