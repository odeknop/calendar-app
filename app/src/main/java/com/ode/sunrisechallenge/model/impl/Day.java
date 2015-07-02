package com.ode.sunrisechallenge.model.impl;

import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ode on 05/06/15.
 */
public class Day implements IDay {

    private long mId;
    private int mDay;
    private int mDayOfWeek;
    private int mMonthOfYear;
    private int mYear;

    public Day(DateTime dt) {
        mDay = dt.getDayOfMonth();
        mDayOfWeek = dt.getDayOfWeek();
        mMonthOfYear = dt.getMonthOfYear();
        mYear = dt.getYear();
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
    public int getMonthOfYear() {
        return mMonthOfYear;
    }

    @Override
    public int getYear() {
        return mYear;
    }

    @Override
    public boolean hasEvents() {
        return false;
    }

    @Override
    public IEvent[] getEvents() {
        return new IEvent[0];
    }

}