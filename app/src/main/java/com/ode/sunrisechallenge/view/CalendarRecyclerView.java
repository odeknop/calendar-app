package com.ode.sunrisechallenge.view;

import android.content.Context;
import android.util.AttributeSet;

import com.ode.sunrisechallenge.recycler.RecyclerView;

/**
 * Created by ode on 01/07/15.
 */
public class CalendarRecyclerView extends RecyclerView {

    public CalendarRecyclerView(Context context) {
        this(context, null, 0);
    }

    public CalendarRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}