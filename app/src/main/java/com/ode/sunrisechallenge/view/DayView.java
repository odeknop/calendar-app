package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;

/**
 * Created by ode on 20/07/15.
 */
public class DayView extends LinearLayout {

    private TextView mEventStart;
    private ImageView mEventIcon;
    private TextView mEventTitle;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.day_view_extended, this, true);
        mEventStart = (TextView) v.findViewById(R.id.event_start);
        mEventIcon = (ImageView) v.findViewById(R.id.event_icon);
        mEventTitle = (TextView) v.findViewById(R.id.event_title);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int h1 = mEventStart.getMeasuredHeight();
        int h2 = mEventIcon.getMeasuredHeight();
        int h3 = mEventTitle.getMeasuredHeight();

        LayoutParams p1 = (LayoutParams) mEventStart.getLayoutParams();
        p1.setMargins(0, (h2 - h1) / 2, 0, 0);
        mEventStart.setLayoutParams(p1);

        LayoutParams p2 = (LayoutParams) mEventTitle.getLayoutParams();
        p2.setMargins(0, (h2 - h3) / 2, 0, 0);
        mEventTitle.setLayoutParams(p2);

        super.onLayout(changed, l, t, r, b);
    }
}
