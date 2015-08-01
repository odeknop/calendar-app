package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.view.drawable.NowDrawable;

/**
 * Created by ode on 25/07/15.
 */
public class DayView extends LinearLayout {

    public static final String TAG = DayView.class.getName();
    private final TextView mEventStart;
    private final TextView mEventTitle;
    private final LinearLayout mEventTime;
    private final int mIconHeight;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(), R.layout.agenda_day_view, this);

        mEventStart = (TextView) findViewById(R.id.event_start);
        mEventTitle = (TextView) findViewById(R.id.event_title);
        mEventTime = (LinearLayout) findViewById(R.id.event_time);
        mIconHeight = getResources().getDimensionPixelSize(R.dimen.day_view_icon_size);

        initLayoutParams();

        setBackgroundDrawable(getResources().getDrawable(R.drawable.agenda_day_view_background));
    }

    private void initLayoutParams() {
        int h1 = (int) (mEventStart.getLineHeight() + 0.20f * mEventStart.getLineHeight());
        int h2 = (int) (mEventTitle.getLineHeight() + 0.20f * mEventTitle.getLineHeight());

        LayoutParams p1 = (LayoutParams) mEventTime.getLayoutParams();
        p1.setMargins(0, (mIconHeight - h1) / 2, 0, 0);
        mEventTime.setLayoutParams(p1);

        LayoutParams p2 = (LayoutParams) mEventTitle.getLayoutParams();
        p2.setMargins(0, (mIconHeight - h2) / 2, 0, 0);
        mEventTitle.setLayoutParams(p2);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setOrientation(LinearLayout.VERTICAL);
    }
}
