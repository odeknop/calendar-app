package com.ode.sunrisechallenge.model.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ode on 26/09/14.
 */
class EventListenersUISafe<T>
{
	private final Object mLockobj = new Object();
	private T[] mListeners_ts;
	private int arrayCount;
	private final T mInvoker;
	private volatile boolean mClosed;
	private final ArrayList<T> list = new ArrayList<>();
	private final Handler handler = new Handler(Looper.getMainLooper());

	private final HashMap<Method,Method> methods = new HashMap<>();

	public EventListenersUISafe(Class<T> classInfo)
	{
		this.mInvoker = buildInvoker(classInfo);
		mListeners_ts = (T[]) Array.newInstance(classInfo, 3);
	}

	public final void add(final T event)
	{
		synchronized(mLockobj)
		{
			removeHandler(event);

			for(int i = 0; i < arrayCount; i++)
			{
				if(mListeners_ts[i] == null /*|| mListeners_ts[i].get() == null*/)
				{
					//mListeners_ts[i] = new WeakReference<T>(event);
					mListeners_ts[i] = event;
					return;
				}
			}

			if(mListeners_ts.length == arrayCount)
				mListeners_ts = Arrays.copyOf(mListeners_ts, (int) (mListeners_ts.length * 1.6));

			mListeners_ts[arrayCount++] = event;
		}
	}

	public final T invoker()
	{
		return mInvoker;
	}

	public static <T> EventListeners<T> create(Class<T> classInfo)
	{
		return new EventListeners<T>(classInfo);
	}

	final void removeAllHandlers()
	{
		synchronized(mLockobj)
		{
			arrayCount = 0;
			Arrays.fill(mListeners_ts, null);
		}
	}

	private void removeHandler(T event)
	{
		synchronized(mLockobj)
		{
			for(int i = 0; i < arrayCount; i++)
			{
				if(mListeners_ts[i] == event)
				{
					mListeners_ts[i] = null;
					if(i == arrayCount - 1)
						arrayCount--;

					return;
				}
			}
		}

	}

	public final int getSize()
	{
		synchronized(mLockobj)
		{
			return arrayCount;
		}
	}

	private void invokeEventHandling(Method method, final Object[] args)
	{
		_callMethod(method, args);
	}

	@SuppressWarnings("unchecked")
	private void _callMethod(Method method, final Object[] args)
	{
		Method unique;
		synchronized(mLockobj)
		{
			if((unique = methods.get(method)) == null)
				methods.put(unique = method, method);
		}

		final Method finalMethod = unique;
		handler.removeMessages(69, method);

		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				actualCall(finalMethod, args);
			}
		};

		Message msg = Message.obtain(handler, runnable);
		msg.obj = method;
		msg.what = 69;

		handler.sendMessageDelayed(msg, 100);
	}

	@SuppressWarnings("unchecked")
	private T buildInvoker(Class<T> classInfo)
	{
		return (T) Proxy.newProxyInstance(classInfo.getClassLoader(), new Class[]{classInfo}, createInvocationHandler());
	}

	private InvocationHandler createInvocationHandler()
	{
		return new InvocationHandler()
		{
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				invokeEventHandling(method, args);
				return null;
			}
		};
	}

	public void close()
	{
		this.removeAllHandlers();
		mClosed = true;
	}

	private void actualCall(Method method, Object[] args)
	{
		list.clear();
		synchronized(mLockobj)
		{
			if(arrayCount == 0)
				return;

			for(int i = 0; i < arrayCount; i++)
			{
				if(mListeners_ts[i] == null) continue;

				T elem = mListeners_ts[i];
				if(elem != null)
					list.add(elem);
			}

		}

		for(T listener : list)
		{
			try
			{
				method.invoke(listener, args);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.e("Listeners", "listenercallback failed", e);
			}
		}

		list.clear();
	}
}
