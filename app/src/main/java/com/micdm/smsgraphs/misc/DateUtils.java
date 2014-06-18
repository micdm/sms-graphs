package com.micdm.smsgraphs.misc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {

    private static final DateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat humanFormat = new SimpleDateFormat("d MMMM");
    private static final DateFormat humanAnotherYearFormat = new SimpleDateFormat("d MMMM yyyy");
    private static final DateFormat humanMonthFormat = new SimpleDateFormat("LLLL");
    private static final DateFormat humanAnotherYearMonthFormat = new SimpleDateFormat("LLLL yyyy");

    public static String formatForDb(Calendar date) {
        return dbFormat.format(date.getTime());
    }

    public static Calendar parseForDb(String string) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dbFormat.parse(string));
            return calendar;
        } catch (ParseException e) {
            throw new RuntimeException(String.format("cannot parse date %s", string));
        }
    }

    public static String formatForHuman(Calendar date) {
        Calendar now = Calendar.getInstance();
        if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            return humanFormat.format(date.getTime());
        }
        return humanAnotherYearFormat.format(date.getTime());
    }

    public static String formatMonthForHuman(Calendar date) {
        Calendar now = Calendar.getInstance();
        if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            return humanMonthFormat.format(date.getTime());
        }
        return humanAnotherYearMonthFormat.format(date.getTime());
    }
}
