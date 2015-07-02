package com.ode.sunrisechallenge.model.impl;

import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IDayManager;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ode on 02/07/15.
 */
public class DayManager implements IDayManager {

    private static IDayManager instance;

    public static IDayManager getInstance() {
        if(instance == null) {
            instance = new DayManager();
        }
        return instance;
    }

    private final IDay[] mDays;

    private DayManager() {

        List<IDay> dates = new ArrayList<IDay>();

        DateTime s = DateTime.now().minusMonths(3);
        while(s.getDayOfWeek() > 1) {
            s = s.minusDays(1);
        }
        DateTime e = DateTime.now().plusYears(1);
        while(e.getDayOfWeek() < 7) {
            e = e.plusDays(1);
        }

        int days = Days.daysBetween(s, e).getDays() + 1;
        for(int i = 0; i < days; i++) {
            DateTime dt = s.withFieldAdded(DurationFieldType.days(), i);
            dates.add(new Day(dt));
        }

        mDays = dates.toArray(new IDay[dates.size()]);
    }

    @Override
    public IDay[] getDays() {
        return mDays;
    }

}
