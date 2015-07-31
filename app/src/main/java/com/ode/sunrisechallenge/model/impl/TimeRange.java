package com.ode.sunrisechallenge.model.impl;

import android.support.annotation.NonNull;

import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

/**
 * Created by ode on 15/07/15.
 */
public class TimeRange implements ITimeRange {

    private final DateTime mStartTime, mEndTime;
    private Duration mDuration;

    public TimeRange(DateTime from, DateTime to) {
        if(!to.isAfter(from)) throw new RuntimeException("StartTime is after EndTime");
        this.mStartTime = from;
        this.mEndTime = to;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ITimeRange && super.equals(o);
    }

    @Override
    public boolean equals(ITimeRange o) {
        return TimeUtils.equals(this, o);
    }

    @Override
    public DateTime getStartTime() {
        return mStartTime;
    }

    @Override
    public DateTime getEndTime() {
        return mEndTime;
    }

    @Override
    public Duration getDuration() {
        if(mDuration == null)
            mDuration = new Duration(mStartTime, mEndTime);
        return mDuration;
    }

    @Override
    public int compareTo(@NonNull ITimeRange another) {
        return this.getStartTime().compareTo(another.getStartTime());
    }

    @Override
    public String toString() {
        return "TimeRange {start=" + mStartTime.toDateTime(DateTimeZone.getDefault()).toString() +
                ", end=" + mEndTime.toDateTime(DateTimeZone.getDefault()).toString() +
                ", duration= " + getDuration().toString() + "}";
    }
}