package com.ode.sunrisechallenge.model.impl;

import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IDayManager;
import com.ode.sunrisechallenge.model.ITimeRange;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ode on 02/07/15.
 */
public class DayManager implements IDayManager {

    private static IDayManager instance;
    private int mIndexOfToday;
    private IDay mYesterday;
    private IDay mToday;
    private IDay mTomorrow;

    public static IDayManager getInstance() {
        if(instance == null) {
            instance = new DayManager();
        }
        return instance;
    }

    private final IDay[] mDays;

    private DayManager() {

        List<IDay> dates = new ArrayList<>();
        ITimeRange calendarRange = Sunrise.getCalendarRange();

        DateTime s = calendarRange.getStartTime();
        while(s.getDayOfWeek() > 1) {
            s = s.minusDays(1);
        }
        DateTime e = calendarRange.getEndTime();
        while(e.getDayOfWeek() < 7) {
            e = e.plusDays(1);
        }
        int days = Days.daysBetween(s, e).getDays() + 1;
        for(int i = 0; i < days; i++) {
            DateTime dt = s.withFieldAdded(DurationFieldType.days(), i);
            if(dt.toLocalDate().equals(DateTime.now().toLocalDate())) {
                mIndexOfToday = i;
                mYesterday = new Day(dt.minusDays(1));
                mToday = new Day(dt);
                mTomorrow = new Day(dt.plusDays(1));
            }
            dates.add(new Day(dt));
        }
        mDays = dates.toArray(new IDay[dates.size()]);
    }

    @Override
    public IDay[] getDays() {
        return mDays;
    }

    @Override
    public int getIndexOfToday() {
        return mIndexOfToday;
    }

    @Override
    public boolean isTomorrow(IDay day) {
        return day.equals(mTomorrow);
    }

    @Override
    public boolean isToday(IDay day) {
        return day.equals(mToday);
    }

    @Override
    public boolean isYesterday(IDay day) {
        return day.equals(mYesterday);
    }
}