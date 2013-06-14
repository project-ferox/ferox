package com.tantaman.ferox.api;

public interface IRouterBuilder {
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandler);
	
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandler);
	
	public IRouter build();
}
