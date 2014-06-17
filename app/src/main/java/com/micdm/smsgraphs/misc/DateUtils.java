package com.micdm.smsgraphs.misc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final DateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat humanThisYearFormat = new SimpleDateFormat("d MMMM");
    private static final DateFormat humanAnotherYearFormat = new SimpleDateFormat("d MMMM yyyy");

    public static String formatForDb(Date date) {
        return dbFormat.format(date);
    }

    public static Date parseForDb(String string) {
        try {
            return dbFormat.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("cannot parse date %s", string));
        }
    }

    public static String formatForHuman(Date date) {
        return ((new Date()).getYear() == date.getYear()) ? humanThisYearFormat.format(date) : humanAnotherYearFormat.format(date);
    }
}
