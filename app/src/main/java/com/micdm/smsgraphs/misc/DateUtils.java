package com.micdm.smsgraphs.misc;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    private static final DateFormat BUNDLE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat DB_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private static final DateFormat HUMAN_FORMAT = new SimpleDateFormat("d MMMM");
    private static final DateFormat HUMAN_ANOTHER_YEAR_FORMAT = new SimpleDateFormat("d MMMM yyyy");
    private static final DateFormat HUMAN_MONTH_FORMAT = new SimpleDateFormat("LLLL");
    private static final DateFormat HUMAN_ANOTHER_YEAR_MONTH_FORMAT = new SimpleDateFormat("LLLL yyyy");

    public synchronized static String formatForBundle(DateTime date) {
        return BUNDLE_FORMAT.format(date.toDate());
    }

    public synchronized static DateTime parseForBundle(String string) {
        try {
            return new DateTime(BUNDLE_FORMAT.parse(string).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(String.format("cannot parse date %s", string));
        }
    }

    public synchronized static String formatForDb(DateTime date) {
        return DB_FORMAT.format(date.toDate());
    }

    public synchronized static DateTime parseForDb(String string) {
        try {
            return new DateTime(DB_FORMAT.parse(string).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(String.format("cannot parse date %s", string));
        }
    }

    public synchronized static String formatForHuman(DateTime date) {
        DateTime now = new DateTime();
        if (date.getYear() == now.getYear()) {
            return HUMAN_FORMAT.format(date.toDate());
        }
        return HUMAN_ANOTHER_YEAR_FORMAT.format(date.toDate());
    }

    public synchronized static String formatMonthForHuman(DateTime date) {
        return formatMonthForHuman(date, false);
    }

    public synchronized static String formatMonthForHuman(DateTime date, boolean needMonthOnly) {
        if (needMonthOnly) {
            return HUMAN_MONTH_FORMAT.format(date.toDate());
        }
        DateTime now = new DateTime();
        if (date.getYear() == now.getYear()) {
            return HUMAN_MONTH_FORMAT.format(date.toDate());
        }
        return HUMAN_ANOTHER_YEAR_MONTH_FORMAT.format(date.toDate());
    }
}
