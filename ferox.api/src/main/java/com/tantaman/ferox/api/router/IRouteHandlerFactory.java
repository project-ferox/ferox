package com.tantaman.ferox.api.router;

/**
 * Every connection requires its own instances of its associated {@link IRouteHandler}s.<br/><br/>
 * 
 * When a new connection is established that matches a given route then all of the {@link IRouteHandler}s
 * for that route are instantiated by calling their associated {@link IRouteHandlerFactory}.
 * 
 * @author tantaman
 *
 */
public interface IRouteHandlerFactory {
	/**
	 * 
	 * @return The {@link IRouteHandler} for the given route
	 */
	public IRouteHandler create();
}
