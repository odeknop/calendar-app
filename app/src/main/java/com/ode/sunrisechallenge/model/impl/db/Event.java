package com.ode.sunrisechallenge.model.impl.db;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.impl.TimeRange;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

import org.joda.time.DateTime;

/**
 * Created by ode on 15/07/15.
 */
public class Event extends DBObject<Event.EventRow> implements IEvent {

    static final class EventRow extends RowContent {

        String title;
        String description;
        String location;
        DateTime startTime;
        DateTime endTime;
        long accountType;
        long accountId;
        String accountEventId;
        private ITimeRange mTimeRange;

        @Override
        protected void setFromCursor(DBHelper parent, Cursor cursor) {
            super.setFromCursor(parent, cursor);

            title = cursor.getString(1);
            description = cursor.getString(2);
            location = cursor.getString(3);
            startTime = TimeUtils.fromDBTime(cursor.getLong(4));
            endTime = TimeUtils.fromDBTime(cursor.getLong(5));
            accountType = cursor.getLong(6);
            accountEventId = cursor.getString(7);
            accountId = cursor.getLong(8);
        }

        ITimeRange getTime() {
            if(mTimeRange == null) mTimeRange = new TimeRange(startTime, endTime);
            return mTimeRange;
        }

    }

    private final Account mAccount;

    protected Event(EventManager eventManager, Cursor cursor) {
        super(EventRow.class, eventManager.parent, cursor);
        mAccount = eventManager.account;
    }

    @Override
    public long getId() {
        return row().id;
    }

    @Override
    public String getTitle() {
        return row().title;
    }

    @Override
    public String getDescription() {
        return row().description;
    }

    @Override
    public ITimeRange getTime() {
        return row().getTime();
    }

    @Override
    public String getLocation() {
        return row().location;
    }

    @Override
    public IAccount getOwnerAccount() {
        return mAccount;
    }

    @Override
    public long getAccountType() {
        return row().accountType;
    }

    @Override
    public String getAccountEventId() {
        return row().accountEventId;
    }

    @Override
    public int compareTo(@NonNull IEvent another) {
        if(this == another) return 0;
        int res = getTime().compareTo(another.getTime());
        if(res == 0) res = getId() < another.getId() ? -1 : 1;
        return res;
    }

    @Override
    public String toString() {
        return String.format("Event {title=%S, startTime=%s, endTime=%s}", getTitle(), getTime().getStartTime().toString(), getTime().getEndTime().toString());
    }
}
