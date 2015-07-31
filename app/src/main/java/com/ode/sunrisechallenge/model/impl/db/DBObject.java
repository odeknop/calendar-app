package com.ode.sunrisechallenge.model.impl.db;

import android.database.Cursor;
import android.util.Log;

import com.ode.sunrisechallenge.model.IComparableValue;
import com.ode.sunrisechallenge.model.utils.Utils;

/**
 * Created by ode on 14/07/15.
 */
class DBObject<T extends RowContent> implements IComparableValue {

    final long id;
    final DBHelper dbHelper;
    private final Class<T> tClass;
    private boolean deleted;

    private volatile T mRef;

    public interface IActivator<T>
    {
        T newInstance(Class<T> tClass, DBHelper dbHelper, Cursor cursor);
    }

    DBObject(Class<T> tClass, DBHelper parent, Cursor cursor) {
        this.dbHelper = parent;
        this.tClass = tClass;
        T res = setFromCursor(cursor);
        setNewRef(res);
        this.id = res.id;
    }

    final T setFromCursor(Cursor cursor) {
        checkDisposed();
        T res = null;
        try {
            res = tClass.newInstance();
            res.setFromCursor(dbHelper, cursor);
        }
        catch(Throwable e) {
            Utils.reThrow(e);
        }
        return res;
    }

    private void checkDisposed() {
        if(deleted)
            throw new RuntimeException("Object is disposed");
        if(dbHelper.disposed)
            throw new RuntimeException("CoreData is disposed");
    }

    void setNewRef(T t) {
        if(t == null)
            deleted = true;
        else if(!deleted)
            mRef = t;
        else
            Log.d("Row", "suspicious call");
    }

    public boolean isAlive() {
        return !deleted;
    }

    boolean exists() {
        T ref = mRef;
        return ref != null && ref.id > 0;
    }

    final T row() {
        return row(true);
    }

    final T row(boolean allowDeleted) {
        if(!allowDeleted) checkDisposed();
        T res = mRef;
        if(res == null) throw new RuntimeException("Trying to make an operation on a non existent object");
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        DBObject object = (DBObject) o;
        T row = row();
        return row.id > 0 && row.id == object.row().id && dbHelper == object.dbHelper;
    }

    @Override
    public int hashCode() {
        return (int) (mRef.id ^ (mRef.id >>> 32));
    }

    @Override
    public long getComparableValue() {
        return id;
    }
}