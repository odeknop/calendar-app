package com.ode.sunrisechallenge;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.support.multidex.MultiDex;

import com.ode.sunrisechallenge.model.ITimeRange;
import com.ode.sunrisechallenge.model.impl.TimeRange;
import com.ode.sunrisechallenge.model.impl.db.DBHelper;
import com.ode.sunrisechallenge.utils.DisplayUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by ode on 01/07/15.
 */
public class Sunrise extends Application {

    public final static int DAYS_IN_A_WEEK = 7;
    public static int ROW_HEIGHT;

    private final static DateTime startTime = DateTime.now().minusMonths(3);
    private final static DateTime endTime = DateTime.now().plusYears(1);


    @Override
    public void onCreate() {
        super.onCreate();
        Point screenSize = DisplayUtils.getScreenDimensions(getApplicationContext());
        ROW_HEIGHT = screenSize.x / DAYS_IN_A_WEEK;
        DBHelper.setApplicationContext(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static ITimeRange getCalendarRange() {
        return new TimeRange(startTime, endTime);
    }
}