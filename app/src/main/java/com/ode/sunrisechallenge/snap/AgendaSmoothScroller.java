package com.ode.sunrisechallenge.snap;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.util.Log;

/**
 * Created by ode on 01/07/15.
 */
public class AgendaSmoothScroller extends LinearSmoothScroller {

    public static final String TAG = AgendaSmoothScroller.class.getName();

    public AgendaSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
        super.onSeekTargetStep(dx, dy, state, action);
        Log.d(TAG, "onSeekTargetStep");
    }

    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        super.onTargetFound(targetView, state, action);
        Log.d(TAG, "onTargetFound");
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        Log.d(TAG, "computeScrollVectorForPosition");
        return new PointF(0, 1);
    }
}
