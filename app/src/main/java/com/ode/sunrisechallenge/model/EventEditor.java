package com.ode.sunrisechallenge.model;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

/**
 * Created by ode on 16/07/15.
 */
public class EventEditor {

    private final String mTitle;
    private final String mDescription;
    private final String mLocation;
    private final DateTime mStartTime;
    private final DateTime mEndTime;
    private final IAccount mAccount;
    private final long mAccountTypeId;
    private final String mAccountEventId;
    private IEvent mEvent;

    public EventEditor(String title, @Nullable String description, @Nullable String location,
                       DateTime startTime, DateTime endTime, IAccount account,
                       long accountType, String accountEventId) {
        mTitle = title;
        mDescription = description == null ? "" : description;
        mLocation = location == null ? "" : location;
        mStartTime = startTime;
        mEndTime = endTime;
        mAccount = account;
        mAccountTypeId = accountType;
        mAccountEventId = accountEventId;
    }

    public EventEditor(IEvent event) {
        mTitle = event.getTitle();
        mDescription = event.getDescription();
        mLocation = event.getLocation();
        mStartTime = event.getTime().getStartTime();
        mEndTime = event.getTime().getEndTime();
        mAccount = event.getOwnerAccount();
        mAccountTypeId = event.getAccountType();
        mAccountEventId = event.getAccountEventId();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public DateTime getStartTime() {
        return mStartTime;
    }

    public DateTime getEndTime() {
        return mEndTime;
    }

    public IAccount getAccount() {
        return mAccount;
    }

    public String getAccountEventId() {
        return mAccountEventId;
    }

    public long getAccountTypeId() {
        return mAccountTypeId;
    }

    public IEvent getSourceEvent() {
        return mEvent;
    }

    public void setSourceEvent(IEvent event) {
        mEvent = event;
    }
}
