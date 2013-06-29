package com.tantaman.ferox.api.router;

public interface IRouteInitializer {
	public static interface Listener {
		public void newRoutesAvailable();
	}
	
	public void addRoutes(IRouterBuilder routerBuilder);
	public int getPriority();
}
