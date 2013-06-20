package com.tantaman.lo4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

public class _ {
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
}
