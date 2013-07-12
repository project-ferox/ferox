package com.tantaman.lo4j;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	
	public static boolean getBool(String b) {
		if (b == null)
			return false;
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String asJsonObject(Object [] keyValPairs) {
		StringBuilder result = new StringBuilder();
		result.append("{");
		
		boolean first = true;
		for (int i = 1; i < keyValPairs.length; i+=2) {
			if (!first) result.append(","); else first = false;
			result.append("\"" + keyValPairs[i-1] + "\":");
			
			if (keyValPairs[i].getClass().isPrimitive()) {
				result.append(keyValPairs[i]);
			} else {
				result.append("\"" + keyValPairs[i] + "\"");
			}
		}
		
		result.append("}");
		
		return result.toString();
	}
	
	// TODO: update webfinger to use this utility
	public static StringBuilder asJsonObject(Map<String, ? extends Object> props, StringBuilder buf) {
		buf.append("{");
		
		boolean first = true;
		for (Map.Entry<String, ? extends Object> entry : props.entrySet()) {
			if (!first) buf.append(","); else first = false;
			
			buf.append("\"").append(entry.getKey()).append("\":");
			
			if (entry.getValue() instanceof Map) {
				asJsonObject((Map<String, Object>)entry.getValue(), buf);
			} else {
				buf.append("\"").append(entry.getValue()).append("\"");
			}
		}
		
		buf.append("}");
		
		return buf;
	}

	public static Map<Object, Object> createMap(Object[] objs) {
		Map<Object, Object> result = new HashMap<>();
		
		for (int i = 1; i < objs.length; i+=2) {
			result.put(objs[i-1], objs[i]);
		}
		
		return result;
	}
	

	public static <P> Collection<P> difference(Collection<P> lhs, Collection<P> rhs) {
		List<P> l = new ArrayList<>(lhs);
		l.removeAll(rhs);
		return l;
	}

	public static <P> Collection<P> reverseDifference(Collection<P> rhs, Collection<P> lhs) {
		return Lo.difference(lhs, rhs);
	}

	public static <P> Collection<P> each(Collection<P> c, Fn<?, P> f) {
		for (P p : c)
			f.f(p);

		return c;
	}

	@SuppressWarnings("unchecked")
	public static <R, P> Collection<R> map(Collection<P> c, Fn<R, P> f) {
		@SuppressWarnings("rawtypes")
		List<R> l = new ArrayList();

		for (P p : c) {
			R item = f.f(p);
			if (item != null)
				l.add(item);
		}

		return l;
	}

	@SuppressWarnings("unchecked")
	public static <R, P> Collection<R> map(Collection<P> c, String f) {
		List<R> result = new ArrayList<>();

		Method m = null;
		try {
			for (P p : c) {
				if (m == null) {
					// TODO: use objectUtils to get the method since this won't check
					// implemented interfaces and parent calsses
					m = p.getClass().getMethod(f);
				}
				R item = (R) m.invoke(p);
				result.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static <P> Object reduce(Collection<P> c, Fn2<Object, Object, P> f) {
		Iterator<P> iter = c.iterator();
		if (!iter.hasNext())
			return null;

		Object result = iter.next();

		return reduce(iter, result, f);
	}


	private static <P> Object reduce(Iterator<P> iter, Object result, Fn2<Object, Object, P> f) {
		while(iter.hasNext()) {
			result = f.f(result, iter.next());
		}

		return result;
	}


	public static <P> Object reduce(Collection<P> c, Object result, Fn2<Object, Object, P> f) {
		return reduce(c.iterator(), result, f);
	}

	public static <P> List<P> distinct(Collection<P> c) {
		Set<P> result = new HashSet<>();

		result.addAll(c);

		return new ArrayList<>(result);
	}

	@SuppressWarnings("unchecked")
	public static <R> R[] drop(R[] a, int n) {
		R[] result = (R[]) Array.newInstance(a[0].getClass(), n);
		System.arraycopy(a, 0, result, 0, n);

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <R> R[] rest(R [] a) {
		R [] result = (R[])Array.newInstance(a[0].getClass(), a.length - 1);

		System.arraycopy(a, 1, result, 0, result.length);
		return result;
	}

	public static <R> R first(List<R> l) {
		return l.get(0);
	}

	public static <R> List<R> last(List<R> l, int n) {
		return l.subList(l.size() - n, l.size());
	}

	public static <R> List<R> first(List<R> l, int n) {
		return l.subList(0, n);
	}

	public static <E> int count(Collection<E> c, Fn<Boolean, E> f) {
		int cnt = 0;
		for (E e : c)
			if (f.f(e))
				++cnt;

		return cnt;
	}

	public static <K,V> Map<K, List<V>> groupBy(Collection<V> c, Fn<K,V> f) {
		Map<K, List<V>> result = new HashMap<>();

		for (V v : c) {
			K key = f.f(v);
			List<V> existing = result.get(key);
			if (existing == null) {
				existing = new ArrayList<>();
				result.put(key, existing);
			}
			existing.add(v);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> Map<T, T> createMapReversed(T... args) {
		if (args ==null || args.length < 2)
			return Collections.EMPTY_MAP;

		Map<T, T> r = new HashMap<>();
		for (int i = 1; i  < args.length; i += 2) {
			r.put(args[i], args[i-1]);
		}

		return r;
	}
	
	@SafeVarargs
	public static <T> List<T> newList(T... args) {
		return Arrays.asList(args);
	}

	public static <T> Set<T> toSet(Collection<T> c) {
		if (c instanceof Set)
			return (Set<T>)c;

		Set<T> r = new HashSet<>();
		r.addAll(c);

		return r;
	}

	public static <K,V> List<V> modifyAndKeep(Set<K> s, Map<K,V> m) {
		List<K> toRemove = new LinkedList<>();
		List<V> removedValues = new LinkedList<>();

		for (K k : m.keySet()) {
			if (!s.contains(k)) {
				toRemove.add(k);
				removedValues.add(m.get(k));
			}
		}

		for (K k : toRemove)
			m.remove(k);

		return removedValues;
	}

	public static <T> Collection<T> keep(Set<T> s, Collection<T> o) {
		List<T> result = new ArrayList<>();

		for (T t : o) {
			if (s.contains(t)) {
				result.add(t);
			}
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P, T> Collection<T> pluck(Collection<P> c, String m, Class<? extends Collection> returnCollectionType) {
		if (c.isEmpty())
			return Collections.emptyList();

		Collection<T> r = Collections.emptyList();

		try {
			r = (Collection<T>)returnCollectionType.newInstance();
			Class<?> clazz = c.iterator().next().getClass();
			Method meth = clazz.getMethod(m);

			for (P i : c) {
				Object picked = meth.invoke(i);
				r.add((T) picked);
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return r;
	}




	// todo indexof
	// todo bin search

	@SuppressWarnings("unchecked")
	public static Collection<Object> flatten(Object c, int depth) {
		if (depth == 0) return Collections.emptyList();

		List<Object> result = new ArrayList<Object>();

		Collection<Object> collection;
		if (c instanceof Map) {
			collection = ((Map<Object, Object>) c).values();
		} else {
			collection = (Collection<Object>) c;
		}

		for (Object o : collection) {
			if (o instanceof Collection) {
				result.addAll(Lo.flatten((Collection<Object>)o, depth - 1));
			} else if (o instanceof Map) {
				// todo flatten values ofmap
				result.addAll(((Map<Object, Object>) o).values());
			} else {
				result.add(o);
			}
		}

		return result;
	}

	public static <K,V> Map<K, V> zipmap(Collection<K> keys, Collection<V> values) {
		Map<K, V> result = new HashMap<K, V>();

		Iterator<K> keyIter = keys.iterator();
		Iterator<V> valIter = values.iterator();

		while(keyIter.hasNext() && valIter.hasNext()) {
			result.put(keyIter.next(), valIter.next());
		}

		return result;
	}

	public static <P> Collection<P> filter(Iterable<P> c, Fn<Boolean, P> f) {
		List<P> result = new ArrayList<>();

		for (P p : c) {
			if (f.f(p)) {
				result.add(p);
			}
		}

		return result;
	}

	public static <P> Collection<P> remove(Collection<P> c, Fn<Boolean, P> f) {
		return filter(c, new Lo.Negate<P>(f));
	}

	public static <P> Collection<List<P>> partition(Collection<P> c, int n) {
		List<List<P>> result = new ArrayList<>();

		int i = 0;
		List<P> segment = null;
		for (P p : c) { 
			if (i % n == 0) {
				segment = new ArrayList<>(n);
				result.add(segment);
			}

			segment.add(p);
		}

		if (Lo.last(result) != segment && segment != null) {
			result.add(segment);
		}

		return result;
	}


	// curry 4 j
	// keep
	// null guard
	// doto
	// twisted
	// combine?  combines colletions? like flatten?
	// mod and keep
	// debounce
	// swing debouce

	public static boolean castAndCompare(Object lhs, Object rhs) {
		if (rhs instanceof Number) {
			Class<?> lhsClass = lhs.getClass();
			if (lhsClass == Double.class) {
				return lhs.equals(((Number) rhs).doubleValue());
			} else if (lhsClass == Float.class) {
				return lhs.equals(((Number) rhs).floatValue());
			} else if (lhsClass == Long.class) {
				return lhs.equals(((Number)rhs).longValue());
			} else if (lhsClass == Integer.class) {
				return lhs.equals(((Number)rhs).intValue());
			} else if (lhsClass == Short.class) {
				return lhs.equals(((Number)rhs).shortValue());
			} else if (lhsClass == Byte.class) {
				return lhs.equals(((Number)rhs).byteValue());
			} else if (lhsClass == Character.class) {
				return lhs.equals(((Number)rhs).byteValue());
			}

			return false;
		} else {
			return lhs.equals(rhs);
		}
	}
	
	public static interface Function {
		
	}

	public static interface Fn<R, P> extends Function {
		public R f(P p);
	}

	public static interface Fn2<R, P1, P2> extends Function {
		public R f(P1 p1, P2 p2);
	}
	
	public static interface VFn<P> extends Function {
		public void f(P p);
	}
	
	public static interface VFn2<P1, P2> extends Function {
		public void f(P1 p1, P2 p2);
	}

	public static class Negate<P> implements Fn<Boolean, P> {
		private final Fn<Boolean, P> wrapped;
		public Negate(Fn<Boolean, P> func) {
			wrapped = func;
		}

		@Override
		public Boolean f(P p) {
			return wrapped.f(p);
		}
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
			if (exec == null)
				exec = SCHEDULED_EXEC;
			
			this.exec = exec;
			this.wrapped = wrapped;
		}
		
		@Override
		public Void f(final P p) {
			if (future != null) {
				future.cancel(true);
			}
			
			future = exec.schedule(new Runnable() {
				@Override
				public void run() {
					wrapped.f(p);
				}
			}, delay, TimeUnit.MILLISECONDS);
			
			return null;
		}
	}
}
