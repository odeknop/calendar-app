package com.ode.sunrisechallenge.model.utils;

import android.database.Cursor;
import android.util.Log;

import java.util.List;

/**
 * Created by ode on 15/07/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();
    public static final String[] EMPTY_STRING_ARR = new String[]{};

    public static <T> T first(List<T> list) {
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }

    public static StringBuilder appendString(StringBuilder source, String toAdd, String delim) {
        if (source == null)
            source = new StringBuilder();
        else if (source.length() != 0)
            source.append(delim);
        return source.append(toAdd);
    }

    public static <T> T useOrCreate(T t, Class<T> tClass) {
        if (t == null) {
            try {
                t = tClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable t) {
                Log.e(TAG, "Error in closeQuietly", t);
            }
        }
    }

    public static String[] arr(long a) {
        return new String[]{Long.toString(a)};
    }

    public static void reThrow(Throwable th) {
        if (th != null) {
            if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            }
            if (th instanceof Error) {
                throw (Error) th;
            }
            throw new RuntimeException(th);
        }
    }

    public static boolean isEmpty(String string) {
        return string != null && string.length() == 0;
    }
}
