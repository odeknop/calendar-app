package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;

/**
 * Created by ode on 01/07/15.
 */
public class SunriseView extends LinearLayout {

    public static final String TAG = SunriseView.class.getName();

    private CalendarViewContainer mCalendarContainer;
    private AgendaViewContainer mAgendaContainer;

    boolean mInitOnce = false;

    public SunriseView(Context context) {
        this(context, null, 0);
    }

    public SunriseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunriseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.sunrise_view, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCalendarContainer = (CalendarViewContainer) findViewById(R.id.calendar_fragment_container);
        mAgendaContainer = (AgendaViewContainer) findViewById(R.id.agenda_fragment_container);
        mAgendaContainer.addEventListener(mCalendarContainer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!mInitOnce) init();
    }

    private void init() {
        mCalendarContainer.getLayoutParams().height = Sunrise.ROW_HEIGHT * 2 + getResources().getDimensionPixelSize(R.dimen.calendar_header_height);
        mInitOnce = true;
    }
}