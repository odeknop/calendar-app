package com.ode.sunrisechallenge.model;

/**
 * Created by ode on 02/07/15.
 */
public interface IDayManager {

    IDay[] getDays();
    int getIndexOfToday();
    boolean isTomorrow(IDay day);
    boolean isToday(IDay day);
    boolean isYesterday(IDay day);
}