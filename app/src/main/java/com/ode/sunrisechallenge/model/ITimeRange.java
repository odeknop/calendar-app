package com.ode.sunrisechallenge.model;

import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Created by ode on 26/06/15.
 */
public interface ITimeRange extends Comparable<ITimeRange> {

    DateTime getStartTime();
    DateTime getEndTime();
    Duration getDuration();
    boolean equals(ITimeRange o);

}