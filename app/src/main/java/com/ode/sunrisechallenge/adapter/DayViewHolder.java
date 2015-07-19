package com.ode.sunrisechallenge.adapter;

import android.view.View;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.recycler.RecyclerView;

/**
 * Created by ode on 26/06/15.
 */
public class DayViewHolder extends RecyclerView.ViewHolder {

    public TextView noEvent;
    public TextView eventTitle;

    public DayViewHolder(View itemView) {
        super(itemView);
        noEvent = (TextView) itemView.findViewById(R.id.no_event);
        eventTitle = (TextView) itemView.findViewById(R.id.event_title);
    }

    public void bind(IEvent event) {
        eventTitle.setText(event.getTitle());
    }
}