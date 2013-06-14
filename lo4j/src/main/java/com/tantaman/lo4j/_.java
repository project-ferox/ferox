package com.tantaman.lo4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
}
