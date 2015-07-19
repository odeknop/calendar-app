package com.ode.sunrisechallenge.model;

import android.os.Parcelable;

/**
 * Created by ode on 13/07/15.
 */
public interface IAccount extends Parcelable {

    long getId();
    String getAccountName();
    IEventManager getEventManager();

}