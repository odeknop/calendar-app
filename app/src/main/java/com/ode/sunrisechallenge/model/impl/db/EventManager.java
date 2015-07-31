package com.ode.sunrisechallenge.model.impl.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.model.EventEditor;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.IEventManager;
import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.model.utils.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by ode on 14/07/15.
 */
public class EventManager extends Tables implements IEventManager {

    final Account account;
    final DBHelper parent;

    EventManager(Account account) {
        this.account = account;
        this.parent = account.dbHelper;
    }

    @Override
    public IEvent[] getEvents(IDay day) {
        ITimeRange range = TimeUtils.getDayRange(day);
        return getEvents(range.getStartTime(), range.getEndTime());
    }

    @Override
    public IEvent[] getEvents(DateTime startTime, DateTime endTime) {
        ArrayList<Event> eventList = rawGetEvents(null, startTime, endTime, true, null, null);
        if(eventList == null) return IEvent.EMPTY_ARR;
        return eventList.toArray(IEvent.EMPTY_ARR);
    }

    @Override
    public IEvent[] getAllEvents() {
        ITimeRange range = Sunrise.getCalendarRange();
        ArrayList<Event> eventList = rawGetEvents(null, range.getStartTime(), range.getEndTime(), true, null, null);
        if(eventList == null) return IEvent.EMPTY_ARR;
        return eventList.toArray(IEvent.EMPTY_ARR);
    }

    @Override
    public boolean hasEvents(IDay day) {
        return getEvents(day).length > 0;
    }

    private Event createEvent(EventEditor eventEditor) {

        Event res = Utils.first(rawGetEvents(eventEditor.getAccountTypeId(), eventEditor.getAccountEventId()));
        if(res != null) return res;
        parent.database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Title", eventEditor.getTitle());
            contentValues.put("Description", eventEditor.getDescription());
            contentValues.put("Location", eventEditor.getLocation());
            contentValues.put("StartTime", TimeUtils.toDBTime(eventEditor.getStartTime()));
            contentValues.put("EndTime", TimeUtils.toDBTime(eventEditor.getEndTime()));
            contentValues.put("AccountTypeId", eventEditor.getAccountTypeId());
            contentValues.put("AccountEventId", eventEditor.getAccountEventId());
            contentValues.put("OwnerAccountId", account.id);

            long id = parent.database.insertWithOnConflict(
                    TABLE_EVENT, null, contentValues,
                    SQLiteDatabase.CONFLICT_NONE
            );
            if (id < 0)
                throw new android.database.SQLException();

            res = Utils.first(rawGetEvents(id));
            if (res == null) throw new RuntimeException();

            parent.database.setTransactionSuccessful();
        } finally {
            parent.database.endTransaction();
        }

        eventEditor.setSourceEvent(res);
        return res;
    }

    private void updateEvent(EventEditor event, Event existing) {
        //TODO implement
    }

    @Override
    public IEvent addOrUpdateEvent(EventEditor event) {
        if(event.getSourceEvent() != null) {
            Event existing = (Event) event.getSourceEvent();
            updateEvent(event, existing);
            return existing;
        } else {
            return createEvent(event);
        }
    }

    private ArrayList<Event> rawGetEvents(Long id, DateTime startTime, DateTime endTime, boolean exclusive, Long accountTypeId, String accountEventId) {
        StringBuilder sel = new StringBuilder();
        ArrayList<String> args = new ArrayList<>();
        extractEventWhereClause(id, startTime, endTime, exclusive, accountTypeId, accountEventId, sel, args);
        DBObject.IActivator<Event> activator = new DBObject.IActivator<Event>() {

            @Override
            public Event newInstance(Class<Event> eventClass, DBHelper dbHelper, Cursor cursor) {
                return new Event(EventManager.this, cursor);
            }
        };
        ArrayList<Event> events = null;
        synchronized (parent.getLock()) {
            Cursor query = parent.database.query(
                    TABLE_EVENT, new String[]
                            {
                                    "EventId", "Title", "Description", "Location", "StartTime",
                                    "EndTime", "AccountTypeId", "AccountEventId", "OwnerAccountId"
                            }, sel.toString(), args.toArray(Utils.EMPTY_STRING_ARR), null, null,
                    null
            );

            try {
                while (query.moveToNext()) {
                    Event ev = parent.nl_updateItem(Event.class, query, activator).commit();
                    if (events == null) events = new ArrayList<>();
                    events.add(ev);
                }
            } finally {
                Utils.closeQuietly(query);
            }
        }
        return events;
    }

    private ArrayList<Event> rawGetEvents(Long id) {
        return rawGetEvents(id, null, null, false, null, null);
    }

    private ArrayList<Event> rawGetEvents(Long accountTypeId, String accountEventId) {
        return rawGetEvents(null, null, null, false, accountTypeId, accountEventId);
    }

    private void extractEventWhereClause(Long id, DateTime from, DateTime to, boolean exclusive, Long accountTypeId, String accountEventId, StringBuilder sel, ArrayList<String> args) {
        Utils.appendString(sel, "OwnerAccountId = ? ", "AND ");
        args.add(Long.toString(account.id));

        if (id != null) {
            Utils.appendString(sel, "EventId = ? ", "AND ");
            args.add(Long.toString(id));
        }

        if (from != null) {
            Utils.appendString(sel, "(StartTime >= ? OR EndTime > ?) ", "AND ");
            args.add(Long.toString(TimeUtils.toDBTime(from)));
            args.add(Long.toString(TimeUtils.toDBTime(from)));
        }

        if (to != null) {
            Utils.appendString(
                    sel, "(EndTime <" + (exclusive ? " " : "= ") +
                            "? OR StartTime <" + (exclusive ? " " : "= ") + " ? ) ", "AND "
            );
            args.add(Long.toString(TimeUtils.toDBTime(to)));
            args.add(Long.toString(TimeUtils.toDBTime(to)));
        }

        if(accountTypeId != null) {
            Utils.appendString(sel, "AccountTypeId = ? ", "AND ");
            args.add(Long.toString(accountTypeId));
        }

        if(accountEventId != null) {
            Utils.appendString(sel, "AccountEventId = ? ", "AND ");
            args.add(accountEventId);
        }
    }

    @Override
    public IAccount getOwner() {
        return account;
    }
}
