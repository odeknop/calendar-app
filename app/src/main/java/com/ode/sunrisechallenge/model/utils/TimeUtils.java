package com.ode.sunrisechallenge.model.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * Created by ode on 26/06/15.
 */
public class TimeUtils {

    public static String getDateAsText(int dayOfWeek, int monthOfYear, int year, int dayOfYear) {
        DateTime date = new DateTime();
        date = date.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfYear).withDayOfWeek(dayOfWeek);
        return date.dayOfWeek().getAsText() + " " + date.dayOfMonth().getAsText() + " " + date.monthOfYear().getAsText();
    }

    public static String getMonthAsShortText(int monthOfYear) {
        LocalDate date = new LocalDate();
        date = date.withMonthOfYear(monthOfYear);
        return date.monthOfYear().getAsShortText();
    }

    public static long toDBTime(DateTime dateTime) {
        return dateTime.toDateTime(DateTimeZone.UTC).getMillis() / 1000L;
    }

    public static DateTime fromDBTime(long time) {
        return new DateTime(time * 1000L, DateTimeZone.UTC);
    }
}