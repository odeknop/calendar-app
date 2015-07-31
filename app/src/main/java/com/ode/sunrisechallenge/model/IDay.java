package com.ode.sunrisechallenge.model;

import org.joda.time.DateTime;

/**
 * Created by ode on 26/06/15.
 */
public interface IDay {

    long getId();
    int getDay();
    int getDayOfWeek();
    int getDayOfMonth();
    int getMonthOfYear();
    int getYear();

    boolean isPast();
    boolean isToday();
    boolean isYesterday();
    boolean isTomorrow();
    boolean isFirstOfMonth();
    boolean isWeekend();
    DateTime getTime();
}