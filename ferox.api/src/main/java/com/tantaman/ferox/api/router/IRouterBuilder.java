package com.tantaman.ferox.api.router;

public interface IRouterBuilder {
	/*
	 * TODO: add upstream and downstream builders?  Orrr  have
	 * IRouteUpstream and IRouteDownstream handler factories??
	 */
	
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandler);
	
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandler);
	
	public IRouter build();
}
