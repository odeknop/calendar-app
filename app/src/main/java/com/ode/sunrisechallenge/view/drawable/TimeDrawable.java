package com.ode.sunrisechallenge.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.util.LayoutDirection;
import android.view.Gravity;

/**
 * Created by ode on 29/07/15.
 */
public class TimeDrawable extends Drawable implements Drawable.Callback
{
	protected final Drawable mDrawable;
	protected final Rect mRect = new Rect();

	public TimeDrawable(Drawable drawable)
	{
		drawable.setCallback(this);
		this.mDrawable = drawable;
	}

	@Override
	public void draw(Canvas canvas)
	{
		mDrawable.draw(canvas);
	}

	@Override
	public void setAlpha(int alpha)
	{
		mDrawable.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		mDrawable.setColorFilter(cf);
	}

	@Override
	public int getOpacity()
	{
		return mDrawable.getOpacity();
	}

	@Override
	protected void onBoundsChange(Rect bounds)
	{
		super.onBoundsChange(bounds);
		GravityCompat.apply(Gravity.RIGHT | Gravity.FILL_VERTICAL, mDrawable.getIntrinsicWidth(), bounds.height(), getBounds(), mRect, LayoutDirection.INHERIT);
		mDrawable.setBounds(mRect);
	}

	/**
	 * Callback
	 */

	@Override
	public void invalidateDrawable(Drawable who)
	{
		if(mDrawable == who && getCallback() != null)
			getCallback().invalidateDrawable(this);
	}

	@Override
	public void scheduleDrawable(Drawable who, Runnable what, long when)
	{
		if(mDrawable == who && getCallback() != null)
			getCallback().scheduleDrawable(this, what, when);
	}

	@Override
	public void unscheduleDrawable(Drawable who, Runnable what)
	{
		if(mDrawable == who && getCallback() != null)
			getCallback().unscheduleDrawable(this, what);
	}

	@Override
	public Drawable mutate()
	{
		Drawable d = mDrawable.mutate();
		if(d == mDrawable) return this;
		return new TimeDrawable(d);
	}
}