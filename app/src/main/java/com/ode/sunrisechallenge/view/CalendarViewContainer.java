package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;

/**
 * Created by ode on 01/07/15.
 */
public class CalendarViewContainer extends FrameLayout implements IChangeListener {

    public static final String TAG = CalendarViewContainer.class.getName();
    boolean mIsActive = false;

    private ResizeAnimation resizeAnimation;
    private ResizeAnimation resetAnimation;

    public CalendarViewContainer(Context context) {
        this(context, null, 0);
    }

    public CalendarViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resizeAnimation = new ResizeAnimation(this, Sunrise.ROW_HEIGHT * 5 + getResources().getDimensionPixelSize(R.dimen.calendar_header_height));
        resizeAnimation.setDuration(1000);
        resetAnimation = new ResizeAnimation(this, Sunrise.ROW_HEIGHT * 2 + getResources().getDimensionPixelSize(R.dimen.calendar_header_height));
        resetAnimation.setDuration(1000);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if(!mIsActive) {
                    this.startAnimation(resizeAnimation);
                    mIsActive = true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public void onLayoutReset() {
        if(mIsActive)
            reset();
    }

    private void reset() {
        this.startAnimation(resetAnimation);
        mIsActive = false;
    }

    class ResizeAnimation extends Animation {
        int targetHeight;
        View view;

        public ResizeAnimation(View view, int targetHeight) {
            this.view = view;
            this.targetHeight = targetHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int initialHeight = view.getLayoutParams().height;
            if(targetHeight > initialHeight) {
                view.getLayoutParams().height = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
            } else {
                view.getLayoutParams().height = initialHeight - (int) ((initialHeight - targetHeight ) * interpolatedTime);
            }
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return false;
        }
    }
}
