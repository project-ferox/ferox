package com.tantaman.ferox;

import java.util.HashMap;
import java.util.Map;

import com.tantaman.ferox.api.IRouteHandlerFactory;
import com.tantaman.ferox.api.IRouter;
import com.tantaman.ferox.api.IRouterBuilder;
import com.tantaman.ferox.priv.router.Route;
import com.tantaman.ferox.priv.trie.Trie;
import com.tantaman.ferox.util.HTTPMethods;

public class RouterBuilder implements IRouterBuilder {
	private final Map<String, Route> routes;
	
	public RouterBuilder() {
		routes = new HashMap<>();
	}
	
	private IRouterBuilder add(String method, String path, IRouteHandlerFactory ... routeHandlers) {
		if (path.charAt(0) == '/')
			path = path.substring(1);
		
		Route route = routes.get(method + path);
		if (route == null) {
			route = new Route(method, path);
			routes.put(method + path, route);
		}
		
		route.addHandlers(routeHandlers);
		return this;
	}
	
	@Override
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandlerFactory) {
		return add(HTTPMethods.GET, path, routeHandlerFactory);
	}

	@Override
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandlerFactory) {
		return add(HTTPMethods.PUT, path, routeHandlerFactory);
	}

	@Override
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandlerFactory) {
		return add(HTTPMethods.POST, path, routeHandlerFactory);
	}

	@Override
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandlerFactory) {
		return add(HTTPMethods.DELETE, path, routeHandlerFactory);
	}

	@Override
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandlerFactory) {
		get(path, routeHandlerFactory);
		put(path, routeHandlerFactory);
		post(path, routeHandlerFactory);
		delete(path, routeHandlerFactory);
		return this;
	}

	@Override
	public IRouter build() {
		return new Router(new Trie(routes.values()));
	}

}
