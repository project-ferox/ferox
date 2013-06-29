package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;

public interface IPluggableRouterBuilder {
	public static interface Listener {
		public void newRoutesStaged(IRouterBuilder routerBuilder);
		public void routesRebuilt(IRouter router);
	}
	
	public void addListener(Listener listener);
	public void addRouteInitializer(final IRouteInitializer routeInitializer);
	public void removeRouteInitializer(final IRouteInitializer routeInitializer);
}
