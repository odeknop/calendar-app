package com.ode.sunrisechallenge.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.ode.sunrisechallenge.recycler.GridLayoutManager;
import com.ode.sunrisechallenge.recycler.RecyclerView;

import org.joda.time.DateTime;

/**
 * Created by ode on 05/06/15.
 */
public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.ViewHolder> {

    public static final String TAG = CalendarViewAdapter.class.getName();

    private IDay[] mDays = DayManager.getInstance().getDays();
    private int mSelectedItem = -1;
    private int mSunriseColor;
    private int mCurrentYear = DateTime.now().getYear();
    private CalendarViewFragment.OnDaySelectedListener mCallback;

    public static final int FIRST_OF_MONTH = 2;
    public static final int REGULAR_DAY = 3;

    public CalendarViewAdapter(Context context, CalendarViewFragment.OnDaySelectedListener callback) {
        mCallback = callback;
        mSunriseColor = context.getResources().getColor(R.color.sunrise);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mDayText.setText(String.valueOf(mDays[position].getDay()));
        holder.itemView.setSelected(mSelectedItem == position);

        if(mSelectedItem == position) {
            holder.itemView.setSelected(true);
            holder.mDayText.setTextColor(Color.WHITE);
        } else if(holder.getItemViewType() == FIRST_OF_MONTH) {
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
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDay() == 1 && mSelectedItem != position ? FIRST_OF_MONTH : REGULAR_DAY;
    }

    private View createViewFor(ViewGroup parent, int viewType) {
        switch (viewType) {
            case REGULAR_DAY: {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_day_view_default, parent, false);
            }
            case FIRST_OF_MONTH: {
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
    }
}