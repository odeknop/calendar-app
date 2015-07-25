package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.utils.DisplayUtils;

/**
 * Created by ode on 25/07/15.
 */
public class DayView extends LinearLayout {

    public static final String TAG = DayView.class.getName();
    private TextView mEventStart;
    private TextView mEventTitle;
    private LinearLayout mEventTime;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.dayViewStyle);

        inflate(getContext(), R.layout.day_view_extended, this);

        mEventStart = (TextView) findViewById(R.id.event_start);
        mEventTitle = (TextView) findViewById(R.id.event_title);
        mEventTime = (LinearLayout) findViewById(R.id.event_time);

        initLayoutParams();
    }

    private void initLayoutParams() {
        int iconHeight = DisplayUtils.dpToPx(getContext(), 30);
        int h1 = (int) (mEventStart.getLineHeight() + 0.20f * mEventStart.getLineHeight());
        int h2 = (int) (mEventTitle.getLineHeight() + 0.20f * mEventTitle.getLineHeight());

        LayoutParams p1 = (LayoutParams) mEventTime.getLayoutParams();
        p1.setMargins(0, (iconHeight - h1) / 2, 0, 0);
        mEventTime.setLayoutParams(p1);

        LayoutParams p2 = (LayoutParams) mEventTitle.getLayoutParams();
        p2.setMargins(0, (iconHeight - h2) / 2, 0, 0);
        mEventTitle.setLayoutParams(p2);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10);
        setLayoutParams(layoutParams);
    }
}
