package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by ode on 26/06/15.
 */
public class AgendaViewAdapter extends RecyclerView.Adapter<DayViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    public static final String TAG = AgendaViewAdapter.class.getName();

    private Context mContext;
    private IDay[] mDays = DayManager.getInstance().getDays();

    public AgendaViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createViewFor(viewType, parent);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        if(holder.getItemViewType() == DayType.HAS_EVENTS) {
            // TODO bind event to day
        }
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
        return getItem(position).hasEvents() ? DayType.HAS_EVENTS : DayType.NO_EVENT;
    }

    private IDay getItem(int position) {
        return mDays[position];
    }

    private View createViewFor(int viewType, ViewGroup parent) {
        switch (viewType) {
            case DayType.NO_EVENT:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_no_event, parent, false);
            case DayType.HAS_EVENTS:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view, parent, false);
        }
        return null;
    }
}