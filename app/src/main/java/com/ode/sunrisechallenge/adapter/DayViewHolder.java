package com.ode.sunrisechallenge.adapter;

import android.view.View;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.recycler.RecyclerView;

/**
 * Created by ode on 26/06/15.
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    public TextView noEvent;
    public TextView eventTitle;
    public TextView eventStart;
    public TextView eventDuration;

    public DayViewHolder(View itemView) {
        super(itemView);
        noEvent = (TextView) itemView.findViewById(R.id.no_event);
        eventTitle = (TextView) itemView.findViewById(R.id.event_title);
        eventStart = (TextView) itemView.findViewById(R.id.event_start);
        eventDuration = (TextView) itemView.findViewById(R.id.event_duration);
    }

    public void bind(IEvent[] event) {
        IEvent ev = event[0];
        eventTitle.setText(ev.getTitle());
        eventStart.setText(ev.getTime().getStartTime().toString("H:mm aa"));
        eventDuration.setText(TimeUtils.getDurationAsString(ev.getTime().getDuration()));
    }
}