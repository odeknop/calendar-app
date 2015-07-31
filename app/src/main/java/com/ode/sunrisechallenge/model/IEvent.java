package com.ode.sunrisechallenge.model;

/**
 * Created by ode on 26/06/15.
 */
public interface IEvent extends Comparable<IEvent> {

    long getId();
    String getTitle();
    String getDescription();
    ITimeRange getTime();
    String getLocation();
    IEvent[] EMPTY_ARR = new IEvent[0];
    IAccount getOwnerAccount();
    long getAccountType();
    String getAccountEventId();
}