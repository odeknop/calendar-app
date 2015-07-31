package com.ode.sunrisechallenge.model.impl;

import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by ode on 05/06/15.
 */
public class Day implements IDay {

    private final long mId;
    private final int mDay;
    private final int mDayOfWeek;
    private final int mDayOfMonth;
    private final int mMonthOfYear;
    private final int mYear;

    private final DateTime dateTime;

    public Day(DateTime dt) {
        dateTime = dt;
        mDay = dt.getDayOfMonth();
        mDayOfWeek = dt.getDayOfWeek();
        mMonthOfYear = dt.getMonthOfYear();
        mYear = dt.getYear();
        mDayOfMonth = dt.getDayOfMonth();
        mId = TimeUtils.toDBTime(dt);
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public int getDay() {
        return mDay;
    }

    @Override
    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    @Override
    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    @Override
    public int getMonthOfYear() {
        return mMonthOfYear;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public boolean isPast() {
        LocalDate localDate = dateTime.toLocalDate();
        return localDate.isBefore(DateTime.now().toLocalDate());
    }

    @Override
    public boolean isToday() {
        LocalDate localDate = dateTime.toLocalDate();
        return localDate.isEqual(DateTime.now().toLocalDate());
    }

    @Override
    public boolean isYesterday() {
        LocalDate localDate = dateTime.toLocalDate();
        return localDate.isEqual(DateTime.now().toLocalDate().minusDays(1));
    }

    @Override
    public boolean isTomorrow() {
        LocalDate localDate = dateTime.toLocalDate();
        return localDate.isEqual(DateTime.now().toLocalDate().plusDays(1));
    }

    @Override
    public boolean isFirstOfMonth() {
        return dateTime.getDayOfMonth() == 1;
    }

    @Override
    public boolean isWeekend() {
        return dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7;
    }

    @Override
    public DateTime getTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        Day d = (Day) o;
        return d.getId() == getId();
    }
}