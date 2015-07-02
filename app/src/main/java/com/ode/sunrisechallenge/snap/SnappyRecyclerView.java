package com.ode.sunrisechallenge.snap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.util.AttributeSet;

/**
 * Created by ode on 29/06/15.
 */
public class SnappyRecyclerView extends RecyclerView {

    private boolean mUserIntercept;

    public SnappyRecyclerView(Context context) {
        super(context);
    }

    public SnappyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SnappyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isUserIntercept() {
        return mUserIntercept;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        final LayoutManager lm = getLayoutManager();

        if (lm instanceof ISnappyLayoutManager) {
            super.smoothScrollToPosition(((ISnappyLayoutManager) getLayoutManager())
                    .getPositionForVelocity(velocityX, velocityY));
            return true;
        }
        return super.fling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // We want the parent to handle all touch events--there's a lot going on there,
        // and there is no reason to overwrite that functionality--bad things will happen.
        final boolean ret = super.onTouchEvent(e);
        final LayoutManager lm = getLayoutManager();

        int action = e.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            mUserIntercept = true;
        } else {
            mUserIntercept = false;
        }

        if (lm instanceof ISnappyLayoutManager
                && (e.getAction() == MotionEvent.ACTION_UP ||
                e.getAction() == MotionEvent.ACTION_CANCEL)
                && getScrollState() == SCROLL_STATE_IDLE) {
            // The layout manager is a SnappyLayoutManager, which means that the
            // children should be snapped to a grid at the end of a drag or
            // fling. The motion event is either a user lifting their finger or
            // the cancellation of a motion events, so this is the time to take
            // over the scrolling to perform our own functionality.
            // Finally, the scroll state is idle--meaning that the resultant
            // velocity after the user's gesture was below the threshold, and
            // no fling was performed, so the view may be in an unaligned state
            // and will not be flung to a proper state.
            smoothScrollToPosition(((ISnappyLayoutManager) lm).getFixScrollPos());
        }



        return ret;
    }
}
