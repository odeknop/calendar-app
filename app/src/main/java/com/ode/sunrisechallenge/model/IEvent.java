package com.ode.sunrisechallenge.model;

import android.os.Parcelable;

import com.ode.sunrisechallenge.model.impl.Location;


/**
 * Created by ode on 26/06/15.
 */
public interface IEvent extends Comparable<IEvent>, Parcelable {

    long getId();
    String getTitle();
    String getDescription();
    boolean isAllDayEvent();
    ITimeRange getTime();
    Location getLocation();
    IEvent[] EMPTY_ARR = new IEvent[0];

}