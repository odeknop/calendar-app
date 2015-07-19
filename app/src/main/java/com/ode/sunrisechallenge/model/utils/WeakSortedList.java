package com.ode.sunrisechallenge.model.utils;

import com.ode.sunrisechallenge.model.IComparableValue;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by adsi on 3/04/2014.
 */
public final class WeakSortedList<T extends IComparableValue> implements Iterable<T> {

    private final ArrayList<Entry<T>> mEntries = new ArrayList<>();
    private final ReferenceQueue<T> mReferenceQueue = new ReferenceQueue<>();

    public void clear() {
        mEntries.clear();
        while (mReferenceQueue.poll() != null) {
        }
    }

    public T get(long compValue) {
        poll();
        int idx = binarySearch(compValue);
        if (idx >= 0) {
            Entry<T> entry = mEntries.get(idx);
            T res = entry.get();
            if (res != null)
                return res;
            mEntries.remove(idx);
        }
        return null;
    }

    public T add(T v) {
        poll();

        T oldValue = null;
        long compValue = v.getComparableValue();
        int idx = binarySearch(compValue);
        if (idx >= 0) {
            Entry<T> entry = mEntries.get(idx);
            oldValue = entry.get();
            mEntries.set(idx, new Entry<>(v, mReferenceQueue, compValue));
        } else
            mEntries.add(~idx, new Entry<>(v, mReferenceQueue, compValue));

        return oldValue;
    }

    public T remove(long compValue) {
        poll();
        int idx = binarySearch(compValue);
        if (idx >= 0) {
            Entry<T> removed = mEntries.remove(idx);
            if (removed != null)
                return removed.get();
        }
        return null;
    }

    public Object[] debugGetAll() {
        ArrayList<T> lst = new ArrayList<T>();
        for (Entry<T> e : mEntries) {
            if (e == null) continue;
            T t = e.get();
            if (t != null)
                lst.add(t);
        }
        return lst.toArray();
    }

    private void poll() {
        Entry<T> toRemove;
        while ((toRemove = (Entry<T>) mReferenceQueue.poll()) != null) {
            int idx = binarySearch(toRemove.compValue);
            if (idx >= 0 && toRemove == mEntries.get(idx))
                mEntries.remove(idx);
        }
    }

    private int binarySearch(long compValue) {
        int low = 0, mid = mEntries.size(), high = mid - 1, result = -1;
        while (low <= high) {
            mid = (low + high) >>> 1;
            if ((result = Long.compare(compValue, mEntries.get(mid).compValue)) > 0) {
                low = mid + 1;
            } else if (result == 0) {
                return mid;
            } else {
                high = mid - 1;
            }
        }
        return -mid - (result < 0 ? 1 : 2);
    }

    private static final class Entry<T> extends WeakReference<T> {
        final long compValue;

        public Entry(T value, ReferenceQueue<? super T> queue, long compValue) {
            super(value, queue);
            this.compValue = compValue;
        }
    }

    private final class It implements Iterator<T> {
        private int nextPos;
        private T nextElem;

        public It() {
            nextPos = -1;
            fill();
        }

        public boolean hasNext() {
            return nextPos < mEntries.size();
        }

        private void fill() {
            nextElem = null;
            while (++nextPos < mEntries.size() && (nextElem = mEntries.get(nextPos).get()) == null) {
            }
        }

        public T next() {
            if (nextPos >= mEntries.size())
                throw new NoSuchElementException();

            T res = nextElem;
            fill();
            return res;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<T> iterator() {
        return new It();
    }
}
