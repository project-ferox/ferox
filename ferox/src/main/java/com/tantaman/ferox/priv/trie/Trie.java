package com.tantaman.ferox.priv.trie;

import java.util.Collection;

import com.tantaman.ferox.api.IRouteSegment;
import com.tantaman.ferox.priv.router.Route;
import com.tantaman.ferox.util.ArrayIterator;

/**
 * Trie must be immutable as the router is constructed once and used
 * for all requests which can be concurrent.
 * @author tantaman
 *
 */
public class Trie {
	private final Node root; 
	
	public Trie(Collection<Route> routes) {
		root = new Node();
		
		for (Route route : routes) {
			addSegments(route);
		}
	}
	
	public IRouteSegment lookup(String method, String route) {
		String [] pieces = route.split("/");
		int start = 0;
		if (pieces.length > 0 && pieces[0].equals("")) {
			start = 1;
		}
		return root.match(method, new ArrayIterator<>(pieces, start));
	}
	
	private void addSegments(Route route) {
		Node cursor = root;
		for (IRouteSegment segment : route) {
			cursor = cursor.addSegment(segment);
		}
	}
}
