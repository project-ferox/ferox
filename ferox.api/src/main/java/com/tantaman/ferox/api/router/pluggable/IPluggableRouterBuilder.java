package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;

/**
 * {@link IPluggableRouterBuilder} instances are notified when {@link IRouteInitializer} services come into and
 * go out of existence within the application.<br/><br/>
 * 
 * When a new {@link IRouteInitializer} service is registered then any {@link IPluggableRouterBuilder}
 * that was constructed via the {@link IPluggableRouterBuilderFactory} are notified and get a chance to add/remove
 * the routes provided by the {@link IRouteInitializer}.
 * 
 * @author tantaman
 *
 */
public interface IPluggableRouterBuilder {
	public static interface Listener {
		public void newRoutesStaged(IRouterBuilder routerBuilder);
		public void routesRebuilt(IRouter router);
	}
	
	public void addListener(Listener listener);
	public void addRouteInitializer(final IRouteInitializer routeInitializer);
	public void removeRouteInitializer(final IRouteInitializer routeInitializer);
}
