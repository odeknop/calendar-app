package com.ode.sunrisechallenge.model.impl.db;

import android.database.Cursor;

/**
 * Created by ode on 14/07/15.
 */
public class RowContent {

    long id;

    protected RowContent() {

    }

    protected void setFromCursor(DBHelper parent, Cursor cursor) {
        this.id = cursor.getLong(0);
    }
}