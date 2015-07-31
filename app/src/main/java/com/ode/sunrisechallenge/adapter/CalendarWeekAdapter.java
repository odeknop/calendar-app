package com.ode.sunrisechallenge.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

/**
 * Created by ode on 05/06/15.
 */
public class CalendarWeekAdapter extends BaseAdapter {

    public static final String TAG = CalendarWeekAdapter.class.getName();

    private enum EViewType {
        Default, Selected, Today, Weekend, PastDay, FirstOfMonth
    }

    private final IDay[] mDays = DayManager.getInstance().getDays();
    private int mSelectedItem = -1;

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public IDay getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        DayViewHolder holder;

        if (view == null) {
            view = createViewFor(getItemViewType(position), parent);
            view.setLayoutParams(new GridView.LayoutParams(Sunrise.ROW_HEIGHT, Sunrise.ROW_HEIGHT));
            holder = new DayViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (DayViewHolder) convertView.getTag();
        }

        holder.bind(getItem(position));

        return view;
    }

    public void setSelected(int position) {
        mSelectedItem = position;
        notifyDataSetChanged();
    }

    @Override

    public int getViewTypeCount() {
        return EViewType.values().length;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == mSelectedItem)
            return EViewType.Selected.ordinal();

        IDay day = getItem(position);

        if (day.isToday()) {
            if(mSelectedItem == -1) {
                return EViewType.Selected.ordinal();
            }
            return EViewType.Today.ordinal();
        }

        if (day.isFirstOfMonth())
            return EViewType.FirstOfMonth.ordinal();

        if (day.isWeekend())
            return EViewType.Weekend.ordinal();

        if (day.isPast())
            return EViewType.PastDay.ordinal();

        return EViewType.Default.ordinal();
    }

    private View createViewFor(int viewType, ViewGroup parent) {
        EViewType type = EViewType.values()[viewType];
        switch (type) {
            case Selected:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_selected, parent, false);
            case Default:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_default, parent, false);
            case Today:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_today, parent, false);
            case PastDay:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_past, parent, false);
            case Weekend:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_weekend, parent, false);
            case FirstOfMonth:
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_month, parent, false);
        }
        return null;
    }

    public static class DayViewHolder {

        public final TextView mDayText;
        public final TextView mMonthName;

        public DayViewHolder(final View itemView) {

            mDayText = (TextView) itemView.findViewById(R.id.day);
            mMonthName = (TextView) itemView.findViewById(R.id.month_name);
        }

        public void bind(IDay day) {
            mDayText.setText(String.valueOf(day.getTime().getDayOfMonth()));
            if (mMonthName != null) mMonthName.setText(TimeUtils.getMonthAsShortText(day.getTime().getMonthOfYear()));
        }
    }
}