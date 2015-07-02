package com.ode.sunrisechallenge;

import android.app.Application;
import android.graphics.Point;

import com.ode.sunrisechallenge.utils.DisplayUtils;

/**
 * Created by ode on 01/07/15.
 */
public class Sunrise extends Application {

    public final static int DAYS_IN_A_WEEK = 7;
    public static int ROW_HEIGHT;


    @Override
    public void onCreate() {
        super.onCreate();

        Point screenSize = DisplayUtils.getScreenDimensions(getApplicationContext());
        ROW_HEIGHT = screenSize.x / DAYS_IN_A_WEEK;
    }
}