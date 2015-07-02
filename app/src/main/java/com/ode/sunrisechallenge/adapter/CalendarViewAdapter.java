package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.fragment.CalendarViewFragment;
import com.ode.sunrisechallenge.model.IDay;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.model.utils.TimeUtils;

import org.joda.time.DateTime;

/**
 * Created by ode on 05/06/15.
 */
public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.ViewHolder> {

    public static final String TAG = CalendarViewAdapter.class.getName();

    private IDay[] mDays = DayManager.getInstance().getDays();
    private int mSelectedItem = -1;
    private int mSunriseColor;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mCurrentYear = DateTime.now().getYear();
    private CalendarViewFragment.OnDaySelectedListener mCallback;

    public CalendarViewAdapter(Context context, CalendarViewFragment.OnDaySelectedListener callback) {
        mCallback = callback;
        mSunriseColor = context.getResources().getColor(R.color.sunrise);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mLayoutManager = recyclerView.getLayoutManager();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mDayText.setText(String.valueOf(mDays[position].getDay()));
        holder.itemView.setSelected(mSelectedItem == position);

        if(mSelectedItem == position) {
            holder.itemView.setSelected(true);
            holder.mDayText.setTextColor(Color.WHITE);
        } else if(holder.getItemViewType() == DayType.FIRST_OF_MONTH) {
            holder.mDayText.setTextColor(mSunriseColor);
            holder.mMonthName.setTextColor(mSunriseColor);
            holder.mYear.setTextColor(mSunriseColor);
            holder.mMonthName.setText(TimeUtils.getMonthAsShortText(getItem(position).getMonthOfYear()));
            holder.mYear.setText(String.valueOf(getItem(position).getYear()));

            if(getItem(position).getYear() > mCurrentYear) {
                holder.mYear.setVisibility(View.VISIBLE);
            } else {
                holder.mYear.setVisibility(View.GONE);
            }

        } else {
            holder.mDayText.setTextColor(Color.BLACK);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = createViewFor(parent, viewType);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        layoutParams.height = Sunrise.ROW_HEIGHT;
        v.setLayoutParams(layoutParams);
        ViewHolder dv = new ViewHolder(v);
        return dv;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDay() == 1 && mSelectedItem != position ? DayType.FIRST_OF_MONTH : DayType.REGULAR_DAY;
    }

    private View createViewFor(ViewGroup parent, int viewType) {
        switch (viewType) {
            case DayType.REGULAR_DAY: {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_default, parent, false);
            }
            case DayType.FIRST_OF_MONTH: {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_year, parent, false);
            }
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mDayText;
        public TextView mMonthName;
        public TextView mYear;


        public ViewHolder(final View itemView) {
            super(itemView);
            mDayText = (TextView) itemView.findViewById(R.id.day);
            mMonthName = (TextView) itemView.findViewById(R.id.month_name);
            mYear = (TextView) itemView.findViewById(R.id.year_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(mSelectedItem);
                    mSelectedItem = getLayoutPosition();
                    notifyItemChanged(mSelectedItem);
                    mCallback.onDaySelected(mSelectedItem);
                }
            });
        }
    }

    private IDay getItem(int position) {
        return mDays[position];
    }

    @Override
    public int getItemCount() {
        return mDays.length;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelected(int selected) {
        notifyItemChanged(mSelectedItem);
        mSelectedItem = selected;
        notifyItemChanged(mSelectedItem);
        mLayoutManager.scrollToPosition(selected);
    }
}