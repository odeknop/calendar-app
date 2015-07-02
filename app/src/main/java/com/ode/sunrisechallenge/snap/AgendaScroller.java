package com.ode.sunrisechallenge.snap;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by ode on 01/07/15.
 */
public class AgendaScroller extends Scroller {

    public AgendaScroller(Context context) {
        super(context);
    }

    public AgendaScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public AgendaScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
}
