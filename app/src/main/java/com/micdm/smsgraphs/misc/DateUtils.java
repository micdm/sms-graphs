package com.micdm.smsgraphs.misc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final DateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat humanThisYearFormat = new SimpleDateFormat("d MMMM");
    private static final DateFormat humanAnotherYearFormat = new SimpleDateFormat("d MMMM yyyy");

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
            return humanThisYearFormat.format(date.getTime());
        }
        return humanAnotherYearFormat.format(date.getTime());
    }
}
