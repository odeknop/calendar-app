package com.ode.sunrisechallenge.snap;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

/**
 * Created by ode on 30/06/15.
 */
public class AgendaRecyclerView extends RecyclerView {

    public static final String TAG = AgendaRecyclerView.class.getName();
    private final int mMinFlingVelocity;
    private final int mMaxFlingVelocity;
    private final ViewFlinger mViewFlinger = new ViewFlinger();
    final Recycler mRecycler = new Recycler();
    final State mState = new State();
    private AgendaLayoutManager mAgendaLayoutManager;

    double FLING_SCALE_DOWN_FACTOR = 0.5;

    private static final Interpolator sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public AgendaRecyclerView(Context context) {
        this(context, null, 0);
    }

    public AgendaRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AgendaRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final ViewConfiguration vc = ViewConfiguration.get(context);
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    public void setLayoutManager() {
        super.setLayoutManager(mAgendaLayoutManager = new AgendaLayoutManager(getContext()));
    }

    private class ViewFlinger implements Runnable {

        private int mLastFlingX;
        private int mLastFlingY;
        private AgendaScroller mScroller;
        private Interpolator mInterpolator = sQuinticInterpolator;

        public ViewFlinger() {
            mScroller = new AgendaScroller(getContext(), sQuinticInterpolator);
        }

        @Override
        public void run() {
            final AgendaScroller scroller = mScroller;

            if (scroller.computeScrollOffset()) {
                final int y = scroller.getCurrY();
                final int dy = y - mLastFlingY;
                int vresult = 0;
                mLastFlingY = y;
                int overscrollY = 0;
                if (getAdapter() != null) {
                    if (dy != 0) {
                        Log.d(TAG, "dy: " + String.valueOf(dy));
                        vresult = mAgendaLayoutManager.scrollVerticallyBy(dy, mRecycler, mState);
                        Log.d(TAG, "vresult: " + String.valueOf(vresult));
                        overscrollY = dy - vresult;
                    }
                }
            }
        }

        public void fling(int velocityX, int velocityY) {
            mLastFlingX = mLastFlingY = 0;
            mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation(this);
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.d(TAG, "velocityY: " + String.valueOf(velocityY));
        velocityX = Math.max(-mMaxFlingVelocity, Math.min(velocityX, mMaxFlingVelocity));
        velocityY = Math.max(-mMaxFlingVelocity, Math.min(velocityY, mMaxFlingVelocity));
        //velocityY *= FLING_SCALE_DOWN_FACTOR;
        //return super.fling(velocityX, velocityY);
        mViewFlinger.fling(velocityX, velocityY);
        return true;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    public class AgendaLayoutManager extends LinearLayoutManager {

        double MANUAL_SCROLL_SLOW_RATIO = 2;

        public final String TAG = AgendaLayoutManager.class.getName();

        public AgendaLayoutManager(Context context) {
            this(context, LinearLayoutManager.VERTICAL, false);
        }

        public AgendaLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public int scrollVerticallyBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state) {
            return super.scrollVerticallyBy(delta, recycler, state);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            AgendaSmoothScroller agendaSmoothScroller = new AgendaSmoothScroller(recyclerView.getContext());
            agendaSmoothScroller.setTargetPosition(position);
            startSmoothScroll(agendaSmoothScroller);
        }
    }
}
