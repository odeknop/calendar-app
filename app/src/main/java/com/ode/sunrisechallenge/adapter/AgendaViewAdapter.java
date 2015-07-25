package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.IDayManager;
import com.ode.sunrisechallenge.model.IEvent;
import com.ode.sunrisechallenge.model.IEventManager;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.ode.sunrisechallenge.recycler.RecyclerView;
import com.ode.sunrisechallenge.recycler.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.ode.sunrisechallenge.view.DayView;

/**
 * Created by ode on 26/06/15.
 */
public class AgendaViewAdapter extends RecyclerView.Adapter<DayViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public static final String TAG = AgendaViewAdapter.class.getName();

    private final IEventManager mEventManager;
    private final IDayManager mDayManager = DayManager.getInstance();
    private IDay[] mDays = mDayManager.getDays();

    public static final int NO_EVENT = 0;
    public static final int HAS_EVENTS = 1;

    private final int mCurrentDayColor;
    private final int mDefaultDayColor;
    private final String mTodayText;
    private final String mTomorrowText;
    private final String mYesterdayText;

    public AgendaViewAdapter(Context context, IEventManager eventManager) {
        mEventManager = eventManager;

        mCurrentDayColor = context.getResources().getColor(R.color.current_day_highlight);
        mDefaultDayColor = context.getResources().getColor(R.color.day_view_extended_header_text);
        mTodayText = context.getResources().getString(R.string.today);
        mYesterdayText = context.getResources().getString(R.string.yesterday);
        mTomorrowText = context.getResources().getString(R.string.tomorrow);
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
        String date = TimeUtils.getDateAsText(day.getDayOfWeek(), day.getMonthOfYear(), day.getYear(), day.getDay());

        if(mDayManager.isToday(day)) {
            textView.setTextColor(mCurrentDayColor);
            date = mTodayText.toUpperCase() + " • " + date;
        }
        else if(mDayManager.isTomorrow(day)) {
            textView.setTextColor(mDefaultDayColor);
            date = mTomorrowText.toUpperCase() + " • " + date;
        }
        else if(mDayManager.isYesterday(day)) {
            date = mYesterdayText.toUpperCase() + " • " + date;
        }
        textView.setText(date);
    }

    @Override
    public void onViewRecycled(DayViewHolder holder) {
        super.onViewRecycled(holder);
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
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_container, parent, false);
        }
        return null;
    }
}