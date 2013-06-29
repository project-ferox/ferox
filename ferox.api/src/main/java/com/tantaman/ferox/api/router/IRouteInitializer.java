package com.tantaman.ferox.api.router;

public interface IRouteInitializer {
	public void addRoutes(IRouterBuilder routerBuilder);
	public int getPriority();
}
