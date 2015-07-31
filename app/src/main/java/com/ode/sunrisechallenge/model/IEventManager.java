package com.ode.sunrisechallenge.model;

import org.joda.time.DateTime;

/**
 * Created by ode on 13/07/15.
 */
public interface IEventManager {

    int GOOGLE = 1;

    IEvent[] getEvents(IDay day);
    IEvent[] getEvents(DateTime startTime, DateTime endTime);
    IEvent[] getAllEvents();
    boolean hasEvents(IDay day);
    IEvent addOrUpdateEvent(EventEditor event);
    IAccount getOwner();
}