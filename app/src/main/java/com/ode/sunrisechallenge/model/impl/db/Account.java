package com.ode.sunrisechallenge.model.impl.db;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.model.IEventManager;

/**
 * Created by ode on 14/07/15.
 */
public class Account extends DBObject<Account.AccountRow> implements IAccount {

    private IEventManager mEventManager;

    Account(DBHelper parent, Cursor cursor) {
        super(AccountRow.class, parent, cursor);
    }

    static final class AccountRow extends RowContent {

        String accountName;

        @Override
        protected void setFromCursor(DBHelper parent, Cursor cursor) {
            super.setFromCursor(parent, cursor);
            accountName = cursor.getString(1);
        }
    }

    @Override
    public IEventManager getEventManager() {
        if(mEventManager == null)
            mEventManager = new EventManager(this);
        return mEventManager;
    }

    @Override
    public String getAccountName() {
        return row().accountName;
    }

    @Override
    public long getId() {
        return row().id;
    }

    @Override
    public String toString() {
        return String.format("Account {name=%s}", getAccountName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long id = row().id;
        dest.writeLong(id);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public IAccount createFromParcel(Parcel in) {
            long id = in.readInt();
            return DBHelper.getInstance().getAccount(id);
        }

        public IAccount[] newArray(int size) {
            return new IAccount[size];
        }
    };
}
