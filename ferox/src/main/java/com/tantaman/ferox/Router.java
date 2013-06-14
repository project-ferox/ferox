package com.tantaman.ferox;

import com.tantaman.ferox.api.IRoute;
import com.tantaman.ferox.api.IRouteSegment;
import com.tantaman.ferox.api.IRouter;
import com.tantaman.ferox.priv.trie.Trie;

/**
 * Keep everything immutable?  Or
 * give them some method of updating the router
 * after it has been constructed?
 * 
 * What's the impact of making the reference
 * to the router volatile in the channel initializer?
 * The thinking being that the router would be immutable but the reference
 * to it wouldn't so you can replace it with a new one.
 */
public class Router implements IRouter {
	private final Trie routes;
	
	public Router(Trie routes) {
		this.routes = routes;
	}
	
	public IRoute lookup(String method, String route) {
		IRouteSegment segment = null;
		try {
			segment = routes.lookup(method, route);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (segment != null)
			return segment.getOwner();
		return null;
	}
}
