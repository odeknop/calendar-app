package com.ode.sunrisechallenge.fragment;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by ode on 14/07/15.
 */
class ApiAsyncEventTask extends AsyncTask<Void, Void, Void> {

    private final LoginFragment mLoginFragment;
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
    protected Void doInBackground(Void... params) {
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
            mError = availabilityException;

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mLoginFragment.startActivityForResult(
                    userRecoverableException.getIntent(),
                    LoginFragment.REQUEST_AUTHORIZATION);
            mError = userRecoverableException;

        } catch (final Exception e) {
            mError = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(mError != null) {
            if(mError instanceof UserRecoverableAuthIOException) {
                mLoginFragment.currentTask = null;
                return;
            }
            mLoginFragment.mStatus.setText("The following error occurred: \n\n" + mError.getMessage());
            mLoginFragment.mStatus.setVisibility(View.VISIBLE);
        } else {
            mLoginFragment.mStatus.setText("Events successfully imported!");
            Toast.makeText(mLoginFragment.getActivity(),
                    mEvents.length > 0 ? mEvents.length + " events imported!" : "No events imported.",
                    Toast.LENGTH_SHORT).show();
            mLoginFragment.startActivity(NavigationUtils.navigateToMainView(mLoginFragment.getActivity(), mLoginFragment.account));
        }
        mLoginFragment.mProgress.setVisibility(View.INVISIBLE);
        mLoginFragment.currentTask = null;
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