package com.ode.sunrisechallenge.model.event;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ode on 26/09/14.
 */
public class EventListeners<T>
{
	protected final Object mLockobj = new Object();
	private final Set<T> mListeners = new HashSet<T>();
	private T[] mListeners_ts;
	private final T mInvoker;
	private final boolean mThreadSafe;
	private final Class<T> mClass;
	protected volatile boolean mClosed;



	public EventListeners(Class<T> classInfo)
	{
		this(classInfo, true);
	}

	@Override
	public String toString()
	{
		return "EventListeners["+ mClass.getSimpleName()+", count="+ mListeners.size()+", mThreadSafe="+ mThreadSafe +"]";
	}

	public EventListeners(Class<T> classInfo, boolean threadsafe)
	{
		this.mClass = classInfo;
		this.mInvoker = buildInvoker(classInfo);
		this.mThreadSafe = threadsafe;
		if(!this.mThreadSafe)
		{
			mListeners_ts = (T[]) Array.newInstance(classInfo, 3);
		}
	}

	public final void add(final T event)
	{
		if (mThreadSafe)
		{
			synchronized (mLockobj)
			{
				if (!mListeners.contains(event))
					mListeners.add(event);
			}
		} else
		{
			if (!mListeners.contains(event))
				mListeners.add(event);
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

	public final void removeAllHandlers()
	{
		if (mThreadSafe)
		{
			synchronized (mLockobj)
			{
				mListeners.clear();
			}
		}
		else
		{
			mListeners.clear();
		}
	}

	public final void removeHandler(T event)
	{
		if (mThreadSafe)
		{
			synchronized (mLockobj)
			{
				mListeners.remove(event);
			}
		} else
			mListeners.remove(event);
	}

	public final int getSize()
	{
		if (mThreadSafe)
		{
			synchronized (mLockobj)
			{
				return mListeners.size();
			}
		} else
			return mListeners.size();
	}

	protected void invokeEventHandling(Method method, Object[] args)
	{
		_callMethod(method, args);
	}

	@SuppressWarnings("unchecked")
	protected final void _callMethod(Method method, Object[] args)
	{
		T[] _listeners;
		if (mThreadSafe)
		{
			synchronized (mLockobj)
			{
				if (mListeners.size() == 0)
					return;

				_listeners = (T[]) new Object[mListeners.size()];
				_listeners = mListeners.toArray(_listeners);
			}

			for (T listener : _listeners)
			{
				try
				{
					method.invoke(listener, args);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Log.e("Listeners", "listenercallback failed", e);
				}
			}
		}
		else
		{
			mListeners_ts = mListeners.toArray(mListeners_ts);
			final int cnt = mListeners.size();
			for(int i = 0; i < cnt; i++)
			{
				try
				{
					method.invoke(mListeners_ts[i], args);
				}
				catch (Exception e)
				{
					if(e.getCause() != null)
						Log.e("Listeners", "listenercallback failed(2)", e.getCause());
					else
						Log.e("Listeners", "listenercallback failed(2)", e);
				}
			}
			Arrays.fill(mListeners_ts, null);
		}
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

	private static final Method equalsMethod = getEqualsMethod();
	private static Method getEqualsMethod()
	{
		try
		{
			return Object.class.getMethod("equals", Object.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static <T> T createListenerWrapper(final Handler h, Class<T> classInfo, final T existing)
	{
		final InvocationHandler invokation = new InvocationHandler()
		{
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
			{
				if(method.equals(equalsMethod))
					return proxy == args[0];

				if (method.getDeclaringClass().equals(Object.class))
				{
					return method.invoke(existing, args);
				}

				final Method _method = method;
				final Object[] _args = args;
				h.post(new Runnable()
				       {

					       public void run()
					       {
						       try
						       {
							       _method.invoke(existing, _args);
						       }
						       catch (Exception e)
						       {
							       e.printStackTrace();
							       Log.e("EventListeners", "createListenerWrapper:call", e);
						       }
					       }
				       });

				return null;
			}
		};

		return (T) Proxy.newProxyInstance(classInfo.getClassLoader(), new Class[]{classInfo}, invokation);
	}

	public void close()
	{
		this.removeAllHandlers();
		mClosed = true;
	}
}
