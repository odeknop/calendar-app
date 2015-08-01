package com.ode.sunrisechallenge.view.drawable;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.util.LayoutDirection;
import android.view.Gravity;

import com.ode.sunrisechallenge.R;

/**
 * Created by ode on 28/07/15.
 */
public class NowDrawable extends TimeDrawable
{
	private final int mHeight;
	private final int mWidth;

	public NowDrawable(Context context)
	{
		super(new TriangleDrawable(context.getResources().getColor(R.color.triangle)));
		this.mHeight = context.getResources().getDimensionPixelSize(R.dimen.triangle_height);
		this.mWidth = context.getResources().getDimensionPixelSize(R.dimen.triangle_width);
	}

	@Override
	protected void onBoundsChange(Rect bounds)
	{
		super.onBoundsChange(bounds);
		GravityCompat.apply(Gravity.CENTER_VERTICAL | Gravity.LEFT, mWidth, mHeight, getBounds(), mRect, LayoutDirection.INHERIT);
		mDrawable.setBounds(mRect);
	}

	@Override
	public int getIntrinsicWidth()
	{
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight()
	{
		return mHeight;
	}
}