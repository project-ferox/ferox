package com.tantaman.ferox;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tantaman.ferox.api.router.IRouteHandlerFactory;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.priv.router.Route;
import com.tantaman.ferox.priv.trie.Trie;
import com.tantaman.ferox.util.HTTPMethods;
import com.tantaman.ferox.util.IPair;
import com.tantaman.ferox.util.Pair;

public class RouterBuilder implements IRouterBuilder {
	protected final Map<String, Route> routes;
	
	public RouterBuilder() {
		routes = new LinkedHashMap<>();
	}
	
	@SafeVarargs
	private final IRouterBuilder add(String method, String path, IPair<Integer, IRouteHandlerFactory> ... routeHandlers) {
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
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandler,
			int priority) {
		return add(HTTPMethods.GET, path, new Pair<>(priority, routeHandler));
	}

	@Override
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandler,
			int priority) {
		return add(HTTPMethods.PUT, path, new Pair<>(priority, routeHandler));
	}

	@Override
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandler,
			int priority) {
		return add(HTTPMethods.POST, path, new Pair<>(priority, routeHandler));
	}

	@Override
	public IRouterBuilder delete(String path,
			IRouteHandlerFactory routeHandler, int priority) {
		return add(HTTPMethods.DELETE, path, new Pair<>(priority, routeHandler));
	}

	@Override
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandler,
			int priority) {
		get(path, routeHandler, priority);
		put(path, routeHandler, priority);
		post(path, routeHandler, priority);
		delete(path, routeHandler, priority);
		return this;
	}
	
	@Override
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandlerFactory) {
		return get(path, routeHandlerFactory, 10);
	}

	@Override
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandlerFactory) {
		return put(path, routeHandlerFactory, 10);
	}

	@Override
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandlerFactory) {
		return post(path, routeHandlerFactory, 10);
	}

	@Override
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandlerFactory) {
		return delete(path, routeHandlerFactory, 10);
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
