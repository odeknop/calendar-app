package com.ode.sunrisechallenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.impl.db.EventManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.model.utils.Utils;
import com.ode.sunrisechallenge.view.DayView;
import com.ode.sunrisechallenge.view.drawable.NowDrawable;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Arrays;

/**
 * Created by ode on 26/06/15.
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    private final TextView noEvent;
    private final View mItemView;
    private static IEvent currentEvent;

    public DayViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        noEvent = (TextView) itemView.findViewById(R.id.no_event);
    }

    public void bind(IEvent[] events, IDay day) {
        LinearLayout container = (LinearLayout) itemView;
        container.removeAllViews();
        Arrays.sort(events);
        currentEvent = EventManager.getOnGoingEvent(events, day);

        for(int i = 0; i < events.length; i++) {
            DayView view = new DayView(mItemView.getContext());
            Holder h = new Holder(view);
            h.bind(events[i]);
            container.addView(view);
            if(i == events.length - 1) view.setBackgroundColor(view.getResources().getColor(R.color.white));
        }
    }

    static final class Holder {

        public final TextView eventTitle;
        public final TextView eventStart;
        public final TextView eventDuration;
        public final TextView soon;

        public Holder(View itemView) {
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventStart = (TextView) itemView.findViewById(R.id.event_start);
            eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
            soon = (TextView) itemView.findViewById(R.id.soon);
        }

        public void bind(IEvent event) {
            String location = null;
            if(!Utils.isEmpty(event.getLocation())) location = " @ " + event.getLocation();
            eventTitle.setText(event.getTitle() + (location == null ? "" : location));
            eventStart.setText(TimeUtils.HOUR_FORMATTER.print(event.getTime().getStartTime()));
            eventStart.setBackgroundDrawable(event == currentEvent ? new NowDrawable(eventStart.getContext()) : null);
            eventDuration.setText(TimeUtils.getDurationAsString(event.getTime().getDuration()));
            if(TimeUtils.isLessThanAnHourFromNow(event)) {
                soon.setVisibility(View.VISIBLE);
                Duration duration = new Duration(DateTime.now(), event.getTime().getStartTime());
                soon.setText("In " + duration.getStandardMinutes() + " min");
            } else {
                soon.setVisibility(View.GONE);
            }
        }
    }
}