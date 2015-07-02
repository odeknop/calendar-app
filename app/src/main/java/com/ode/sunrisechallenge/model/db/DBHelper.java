package com.ode.sunrisechallenge.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ode.sunrisechallenge.model.IData;

/**
 * Created by ode on 26/06/15.
 */
public class DBHelper implements IData {

    private static Context mContext;
    private static final Object mLock = new Object();
    private static volatile IData mDataInstance;
    final private SQLiteDatabase mDatabase;

    private final static String DATABASE_NAME = "challenge_sunrise";

    public static IData getInstance() {
        return getInstance(mContext);
    }

    public static IData getInstance(Context context) {
        if (context == null) {
            throw new RuntimeException("Context must be set before using the database");
        }

        IData instance = mDataInstance;

        if(instance != null) {
            return instance;
        }

        synchronized (mLock) {
            if (mDataInstance == null) {
                mDataInstance = new DBHelper(context);
            }
            return mDataInstance;
        }
    }

    public void setApplicationContext(Context context) {
        mContext = context;
    }

    private DBHelper(Context context) {
        mDatabase = new OpenHelper(context, DATABASE_NAME).getWritableDatabase();
    }
}