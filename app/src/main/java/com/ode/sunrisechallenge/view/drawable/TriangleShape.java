package com.ode.sunrisechallenge.view.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by ode on 28/07/15.
 */
public class TriangleShape extends Shape
{
	private final Path mPath;

	public TriangleShape()
	{
		this.mPath = new Path();
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		mPath.lineTo(0, getHeight());
		mPath.lineTo(getWidth(), getHeight() / 2);
		mPath.lineTo(0, 0);
		canvas.drawPath(mPath, paint);
	}
}