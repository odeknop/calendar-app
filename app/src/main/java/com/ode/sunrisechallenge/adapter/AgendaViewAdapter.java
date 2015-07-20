package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IData;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.IEventManager;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.recycler.RecyclerView;
import com.ode.sunrisechallenge.recycler.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by ode on 26/06/15.
 */
public class AgendaViewAdapter extends RecyclerView.Adapter<DayViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public static final String TAG = AgendaViewAdapter.class.getName();

    private final IEventManager mEventManager;
    private IDay[] mDays = DayManager.getInstance().getDays();

    public static final int NO_EVENT = 0;
    public static final int HAS_EVENTS = 1;

    public AgendaViewAdapter(IEventManager eventManager) {
        mEventManager = eventManager;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(DayManager.getInstance().getIndexOfToday());
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createViewFor(viewType, parent);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        IDay day = getItem(position);
        if(holder.getItemViewType() == HAS_EVENTS) {
            IEvent[] events = mEventManager.getEvents(day);
            holder.bind(events);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) { };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        IDay day = getItem(position);
        textView.setText(TimeUtils.getDateAsText(day.getDayOfWeek(), day.getMonthOfYear(), day.getYear(), day.getDay()));
    }

    @Override
    public int getItemCount() {
        return mDays.length;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mEventManager.hasEvents(getItem(position)) ? HAS_EVENTS : NO_EVENT;
    }

    private IDay getItem(int position) {
        return mDays[position];
    }

    private View createViewFor(int viewType, ViewGroup parent) {
        switch (viewType) {
            case NO_EVENT:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_no_event, parent, false);
            case HAS_EVENTS:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view, parent, false);
        }
        return null;
    }
}