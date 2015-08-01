package com.ode.sunrisechallenge.view.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by ode on 28/07/15.
 */
public class TriangleDrawable extends ShapeDrawable
{
	private final Paint mPaint;

	public TriangleDrawable(int color)
	{
		super(new TriangleShape());
		mPaint = new Paint();
		mPaint.setColor(color);
	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint)
	{
		super.onDraw(shape, canvas, mPaint);
	}
}