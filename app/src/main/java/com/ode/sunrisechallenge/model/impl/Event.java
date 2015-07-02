package com.ode.sunrisechallenge.model.impl;

import org.joda.time.DateTime;

import java.util.TimeZone;

/**
 * Created by ode on 26/06/15.
 */
public class Event {

    private String mTitle;
    private String mDesciption;

    private boolean mIsAllDayEvent;
    private Location mLocation;

    private DateTime mCreated;
    private DateTime mUpdated;

    private DateTime mStartTime;
    private DateTime mEndTime;
    private TimeZone mTimeZone;

    public Event(String title) {
        mTitle = title;
    }
}