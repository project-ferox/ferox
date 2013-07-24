package com.tantaman.ferox.api.router;

/**
 * Interface for a router.  Provides a means to look up a route
 * for a specific path and method.
 * @author tantaman
 *
 */
public interface IRouter {
	public IRoute lookup(String method, String route);
}
