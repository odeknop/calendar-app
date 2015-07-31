package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by ode on 26/06/15.
 */
public class AgendaAdapter extends RecyclerView.Adapter<DayViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public static final String TAG = AgendaAdapter.class.getName();

    private enum EViewType {
        NoEvent, Default, TVShow
    }

    private final IEventManager mEventManager;
    private final IDayManager mDayManager = DayManager.getInstance();
    private final IDay[] mDays = mDayManager.getDays();

    private final int mCurrentDayColor;
    private final int mDefaultDayColor;
    private final String mTodayText;
    private final String mTomorrowText;
    private final String mYesterdayText;

    public AgendaAdapter(Context context, IEventManager eventManager) {
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
        EViewType type = EViewType.values()[holder.getItemViewType()];
        switch (type) {
            case Default: {
                IEvent[] events = mEventManager.getEvents(day);
                holder.bind(events);
            }
        }
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_day_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) { };
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        IDay day = getItem(position);
        String date = TimeUtils.getDateAsText(day.getDayOfWeek(), day.getMonthOfYear(), day.getYear(), day.getDay());

        if(day.isToday()) {
            textView.setTextColor(mCurrentDayColor);
            date = mTodayText.toUpperCase() + " • " + date;
        }
        else if(day.isTomorrow()) {
            textView.setTextColor(mDefaultDayColor);
            date = mTomorrowText.toUpperCase() + " • " + date;
        }
        else if(day.isYesterday()) {
            date = mYesterdayText.toUpperCase() + " • " + date;
        }
        textView.setText(date);
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
        if(mEventManager.hasEvents(getItem(position)))
            return EViewType.Default.ordinal();
        else
            return EViewType.NoEvent.ordinal();
    }

    private IDay getItem(int position) {
        return mDays[position];
    }

    private View createViewFor(int viewType, ViewGroup parent) {
        EViewType type = EViewType.values()[viewType];
        switch (type) {
            case NoEvent:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_day_view_no_event, parent, false);
            case Default:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_day_view_event_container, parent, false);
            case TVShow:
                return null;
        }
        return null;
    }
}