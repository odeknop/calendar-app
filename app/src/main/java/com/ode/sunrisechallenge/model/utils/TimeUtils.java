package com.ode.sunrisechallenge.model.utils;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.impl.TimeRange;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by ode on 26/06/15.
 */
public class TimeUtils {

    private static final LocalTime mStartTime = new LocalTime(0, 0); //00H00 AM
    private static final LocalTime mEndTime = new LocalTime(23, 0); //24H00 AM

    private static final PeriodFormatter mPeriodFormatter = new PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix("d")
            .appendHours()
            .appendSuffix("h")
            .appendMinutes()
            .appendSuffix("m")
            .toFormatter();

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

    public static boolean equals(ITimeRange timeRange, ITimeRange otherTimeRange) {
        return timeRange.getStartTime().equals(otherTimeRange.getStartTime())
                && timeRange.getEndTime().equals(otherTimeRange.getEndTime());
    }

    private static DateTime combine(LocalDate date, LocalTime time) {
        return new DateTime(
                date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
                time.getHourOfDay(), time.getMinuteOfHour()
        ).toDateTime(DateTimeZone.UTC);
    }

    private static ITimeRange startOfDayRange(LocalDate dt) {
        DateTime from = combine(dt, mStartTime);
        DateTime to = combine(dt.plusDays(1), mStartTime);
        return new TimeRange(from, to);
    }

    public static ITimeRange getDayRange(IDay day) {
        LocalDate date = new LocalDate(day.getYear(), day.getMonthOfYear(), day.getDay());
        return startOfDayRange(date);
    }

    public static ITimeRange getTimeRange(Event event) {
        if(event.getStart().getDateTime() == null || event.getEnd().getDateTime() == null) return null;
        EventDateTime start = event.getStart();
        EventDateTime end = event.getEnd();
        return new TimeRange(new DateTime(start.getDateTime().getValue(), DateTimeZone.forID(start.getTimeZone())),
                new DateTime(end.getDateTime().getValue(), DateTimeZone.forID(end.getTimeZone())));
    }

    public static String getDurationAsString(Duration duration) {
        Period p = duration.toPeriod();
        return p.toString(mPeriodFormatter);
    }
}