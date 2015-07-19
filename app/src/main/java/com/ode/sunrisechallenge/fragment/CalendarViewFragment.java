package com.ode.sunrisechallenge.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.adapter.CalendarViewAdapter;
import com.ode.sunrisechallenge.model.IDayManager;
import com.ode.sunrisechallenge.model.impl.DayManager;
import com.ode.sunrisechallenge.recycler.GridLayoutManager;
import com.ode.sunrisechallenge.recycler.RecyclerView;
import com.ode.sunrisechallenge.view.CalendarRecyclerView;

public class CalendarViewFragment extends Fragment {

    public static final String TAG = CalendarViewFragment.class.getName();

    private OnDaySelectedListener mCallback;
    private CalendarViewAdapter mCalendarViewAdapter;
    private GridLayoutManager mGridLayoutManager;
    private CalendarRecyclerView mRecyclerView;

    public static CalendarViewFragment newInstance() {
        return new CalendarViewFragment();
    }

    public interface OnDaySelectedListener {
        void onDaySelected(int position);
    }

    public CalendarViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnDaySelectedListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement OnDaySelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (CalendarRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager = new GridLayoutManager(getActivity(), Sunrise.DAYS_IN_A_WEEK));
        mRecyclerView.addItemDecoration(new DayViewItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mCalendarViewAdapter = new CalendarViewAdapter(view.getContext(), mCallback));
        mRecyclerView.getLayoutManager().scrollToPosition(50);
    }

    public void setSelected(int position, boolean scrolling) {
        if(mCalendarViewAdapter.getSelectedItem() != position && scrolling) {
            if(position > mGridLayoutManager.findLastCompletelyVisibleItemPosition() ||
                    position < mGridLayoutManager.findFirstCompletelyVisibleItemPosition()) {
                mRecyclerView.scrollToPosition(position);
            }
            mCalendarViewAdapter.setSelected(position);
        }
    }

    public class DayViewItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mWeekDivider;

        public DayViewItemDecoration(Context context) {
            mWeekDivider = context.getResources().getDrawable(R.drawable.day_view_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = params.bottomMargin + child.getBottom();
                int bottom = top + mWeekDivider.getIntrinsicHeight();

                mWeekDivider.setBounds(left, top, right, bottom);
                mWeekDivider.draw(c);
            }
        }
    }
}