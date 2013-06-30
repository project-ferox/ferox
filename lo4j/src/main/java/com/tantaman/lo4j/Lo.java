package com.tantaman.lo4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Lo {
	private static final ScheduledExecutorService SCHEDULED_EXEC = Executors.newScheduledThreadPool(1);
	
	public static <T> T first(Collection<T> c) {
		return c.iterator().next();
	}
	
	public static <T> T last(List<T> c) {
		return c.listIterator(c.size()).previous();
	}
	
	public static <T> T last(Collection<T> c) {
		if (c instanceof List) {
			return last((List<T>)c);
		}
		
		Iterator<T> i = c.iterator();
		
		T last = null;
		while (i.hasNext()) {
			last = i.next();
		}
		
		return last;
	}

	public static Map<Object, Object> createMap(Object[] objs) {
		Map<Object, Object> result = new HashMap<>();
		
		for (int i = 1; i < objs.length; i+=2) {
			result.put(objs[i-1], objs[i]);
		}
		
		return result;
	}
	
	public static <P> Lo.Fn<Void, P> debounce(Lo.Fn<?, P> f, long time, TimeUnit unit) {
		return new DebouncedFn<P>(f, TimeUnit.MILLISECONDS.convert(time, unit), SCHEDULED_EXEC);
	}
	
	public static <P> Lo.Fn<Void, P> debounce(Lo.Fn<?, P> f, long time, TimeUnit unit, ScheduledExecutorService exec) {
		return new DebouncedFn<P>(f, TimeUnit.MILLISECONDS.convert(time, unit), exec);
	}
	
	private static class DebouncedFn<P> implements Lo.Fn<Void, P> {
		private volatile ScheduledFuture<?> future;
		private final long delay;
		private final ScheduledExecutorService exec;
		private final Lo.Fn<?, P> wrapped;
		
		public DebouncedFn(Lo.Fn<?, P> wrapped, long delay, ScheduledExecutorService exec) {
			this.delay = delay;
			this.exec = exec;
			this.wrapped = wrapped;
		}
		
		@Override
		public Void f(final P p) {
			if (future != null) {
				future.cancel(true);
			}
			
			future = SCHEDULED_EXEC.schedule(new Runnable() {
				@Override
				public void run() {
					wrapped.f(p);
				}
			}, delay, TimeUnit.MILLISECONDS);
			
			return null;
		}
	}
	
	public static interface Fn<R, P> {
		public R f(P p);
	}
}
