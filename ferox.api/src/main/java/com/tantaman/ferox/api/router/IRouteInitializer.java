package com.tantaman.ferox.api.router;

/**
 * Service interface for classes that wish to dynamically add routes to Ferox.<br/><br/>
 * 
 * Whenever an {@link IRouteInitializer} service is registered
 * its <code>addRoutes</code> method is called with the {@link IRouterBuilder} for the current
 * instance of Ferox.
 * 
 * @author tantaman
 *
 */
public interface IRouteInitializer {
	public static interface Listener {
		public void newRoutesAvailable();
	}
	
	/**
	 * Called after the {@link IRouteInitializer} has been registered with the OSGi service registry.
	 * 
	 * Gives this {@link IRouteInitializer} as way to register its routes with Ferox.
	 * 
	 * @param routerBuilder
	 */
	public void addRoutes(IRouterBuilder routerBuilder);
	public int getPriority();
}
