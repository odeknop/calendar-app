package com.ode.sunrisechallenge.model;

import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by ode on 26/06/15.
 */
public interface ITimeRange extends Comparable<ITimeRange>, Parcelable {

    DateTime getStartTime();
    DateTime getEndTime();

}