package com.ode.sunrisechallenge.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by ode on 13/07/15.
 */
public interface IEventManager {

    int GOOGLE = 1;

    IEvent[] getEvents(IDay day);
    IEvent[] getEvents(DateTime startTime, DateTime endTime);
    boolean hasEvents(IDay day);
    IEvent addOrUpdateEvent(EventEditor event);
}