package com.tantaman.ferox.priv.trie;

import com.tantaman.ferox.priv.router.RouteSegment;


/**
 * Map.get(o) does a:
 * o.equals(e);
 * @author tantaman
 *
 */
public class StringToStandardRouteSegmentLookup {
	private final String delegate;
	
	public StringToStandardRouteSegmentLookup(String delegate) {
		if (delegate == null) {
			System.out.print("");
		}
		this.delegate = delegate;
	}
	
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj.getClass() == RouteSegment.class) {
			return delegate.equals(((RouteSegment)obj).getString());
		}
		
		return false;
	}
}
