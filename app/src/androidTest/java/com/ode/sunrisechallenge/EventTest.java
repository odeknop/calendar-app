package com.ode.sunrisechallenge;

import android.test.AndroidTestCase;

import com.ode.sunrisechallenge.model.EventEditor;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.IEventManager;
import com.ode.sunrisechallenge.model.impl.db.DBHelper;
import com.ode.sunrisechallenge.model.impl.db.EventManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by ode on 30/07/15.
 */
public class EventTest extends AndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DBHelper.debugRemoveDBAndResetInstance();
    }

    public IEventManager createEventManager() throws Exception {
        String name =  "odeknop@gmail.com";
        IAccount account = DBHelper.getInstance().createAccount(name);
        IEventManager eventManager = account.getEventManager();
        assertEquals(account, eventManager.getOwner());
        return eventManager;
    }

    public void testCreateEvent() throws Exception {
       createEvent();
    }

    public IEvent createEvent() throws Exception {
        IEventManager eventManager = createEventManager();
        String eventTitle = "test event";
        String eventDescription = "description of test event";
        String location = "Brussels";
        DateTime startTime = DateTime.now(DateTimeZone.UTC);
        DateTime endTime = DateTime.now(DateTimeZone.UTC).plusHours(1);
        String eventId = eventTitle + eventDescription;

        EventEditor eventEditor = new EventEditor(eventTitle, eventDescription, location, startTime, endTime, eventManager.getOwner(), EventManager.GOOGLE, eventId);
        IEvent createdEvent = eventManager.addOrUpdateEvent(eventEditor);
        assertNotNull(createdEvent);

        checkEventValues(createdEvent, eventEditor);
        IEvent[] allEvents = eventManager.getAllEvents();
        assertTrue(allEvents.length == 1);
        assertEquals(createdEvent, allEvents[0]);
        return createdEvent;
    }

    public static void checkEventValues(IEvent created, EventEditor event) {
        assertEquals(event.getTitle(), created.getTitle());
        assertEquals(event.getDescription(), created.getDescription());
        assertEquals(event.getLocation(), created.getLocation());
        assertEquals(event.getAccountEventId(), created.getAccountEventId());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DBHelper.debugRemoveDBAndResetInstance();
    }
}
