package com.ode.sunrisechallenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.model.utils.Utils;
import com.ode.sunrisechallenge.view.DayView;

/**
 * Created by ode on 26/06/15.
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    private final TextView noEvent;
    private final View mItemView;

    public DayViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        noEvent = (TextView) itemView.findViewById(R.id.no_event);
    }

    public void bind(IEvent[] events) {
        LinearLayout container = (LinearLayout) itemView;
        container.removeAllViews();
        for(IEvent event : events) {
            DayView view = new DayView(mItemView.getContext());
            Holder h = new Holder(view);
            h.bind(event);
            container.addView(view);
        }
    }

    static final class Holder {

        public final TextView eventTitle;
        public final TextView eventStart;
        public final TextView eventDuration;

        public Holder(View itemView) {
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventStart = (TextView) itemView.findViewById(R.id.event_start);
            eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
        }

        public void bind(IEvent event) {
            String location = null;
            if(!Utils.isEmpty(event.getLocation())) location = " @ " + event.getLocation();
            eventTitle.setText(event.getTitle() + (location == null ? "" : location));
            eventStart.setText(event.getTime().getStartTime().toString("H:mm aa"));
            eventDuration.setText(TimeUtils.getDurationAsString(event.getTime().getDuration()));
        }
    }
}