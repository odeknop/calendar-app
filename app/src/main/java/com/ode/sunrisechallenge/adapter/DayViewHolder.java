package com.ode.sunrisechallenge.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.recycler.RecyclerView;
import com.ode.sunrisechallenge.view.DayView;

/**
 * Created by ode on 26/06/15.
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    public TextView noEvent;
    private View mItemView;

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

        public TextView eventTitle;
        public TextView eventStart;
        public TextView eventDuration;

        public Holder(View itemView) {
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventStart = (TextView) itemView.findViewById(R.id.event_start);
            eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
        }

        public void bind(IEvent event) {
            eventTitle.setText(event.getTitle());
            eventStart.setText(event.getTime().getStartTime().toString("H:mm aa"));
            eventDuration.setText(TimeUtils.getDurationAsString(event.getTime().getDuration()));
        }
    }
}