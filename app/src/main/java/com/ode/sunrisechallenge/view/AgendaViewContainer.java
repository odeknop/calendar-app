package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.ode.sunrisechallenge.model.event.EventListenersUISafe;

/**
 * Created by ode on 01/07/15.
 */
public class AgendaViewContainer extends FrameLayout {

    EventListenersUISafe<IChangeListener> listeners = new EventListenersUISafe<>(IChangeListener.class);

    public AgendaViewContainer(Context context) {
        this(context, null, 0);
    }

    public AgendaViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgendaViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addEventListener(IChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                listeners.invoker().onLayoutReset();
                return false;
            }
        }
        return false;
    }
}