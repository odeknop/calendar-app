package com.ode.sunrisechallenge.fragment;

import android.os.AsyncTask;
import android.view.View;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.client.util.DateTime;
import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.model.EventEditor;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.IEventManager;
import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.utils.NavigationUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by ode on 14/07/15.
 */
public class ApiAsyncEventTask extends AsyncTask<Void, Void, Boolean> {

    LoginFragment mLoginFragment;
    private IEvent[] mEvents;
    private Exception mError;

    public ApiAsyncEventTask(LoginFragment loginFragment) {
        mLoginFragment = loginFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoginFragment.mProgress.setVisibility(View.VISIBLE);
        mLoginFragment.mStatus.setText("Importing events...");
        mLoginFragment.mStatus.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        mError = null;
        try {
            final List<Event> results = getDataFromApi();

            IEventManager eventManager = mLoginFragment.account.getEventManager();

            for(Event event : results) {
                if(event.getStart().getDateTime() != null) {
                    ITimeRange range = TimeUtils.getTimeRange(event);
                    EventEditor ev = new EventEditor(event.getSummary(), event.getDescription(), event.getLocation(),
                            range != null ? range.getStartTime() : null, range != null ? range.getEndTime() : null, mLoginFragment.account, IEventManager.GOOGLE, event.getId());
                    eventManager.addOrUpdateEvent(ev);
                }
            }

            mEvents = eventManager.getEvents(Sunrise.getCalendarRange().getStartTime(), Sunrise.getCalendarRange().getEndTime());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mLoginFragment.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mLoginFragment.startActivityForResult(
                    userRecoverableException.getIntent(),
                    LoginFragment.REQUEST_AUTHORIZATION);

        } catch (final Exception e) {
            mError = e;
            return false;
        }
        return mEvents.length > 0;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        mLoginFragment.mProgress.setVisibility(View.INVISIBLE);
        if(mError != null) {
            mLoginFragment.mStatus.setText("The following error occurred: " +
                    mError.getMessage());
        } else {
            mLoginFragment.mStatus.setText(success ? mEvents.length + " events imported!" : "No events imported");
        }
        mLoginFragment.mStatus.setVisibility(View.VISIBLE);
    }

    private List<Event> getDataFromApi() throws IOException {
        DateTime start = new DateTime(Sunrise.getCalendarRange().getStartTime().getMillis());
        DateTime end = new DateTime(Sunrise.getCalendarRange().getEndTime().getMillis());
        Events events = mLoginFragment.service.events().list("primary")
                .setTimeMin(start)
                .setTimeMax(end)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return events.getItems();
    }
}