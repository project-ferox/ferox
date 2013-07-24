package com.tantaman.ferox.api.router;

import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;

/**
 * Allows users to add routes to Ferox's router.<br/><br/>
 * 
 * <code>routerBuilder.get(...)</code> adds routes and handlers that respond to HTTP GET requests.<br/>
 * <code>routerBuilder.put(...)</code> adds routes and handlers that responsd to HTTP PUT requests.<br/>
 * etc.
 * <br/><br/>
 * 
 * <code>
 * routerBuilder.get("/home/:user/**", userReadHandlerFactory);
 * routerBuilder.put("/home/:user/**", userWriteHandlerFactory);
 * </code>
 * 
 * <br/><br/>
 * Multiple handlers can be added to the same route.  When this happens an {@link IRouteHandler} can
 * called it's "next" parameter to pass control on to the next handler in the route.  If there is not
 * another handler then calling next will just be a no-op.<br/><br/>
 * 
 * <code>
 * routerBuilder.get("/home/:user/**", authenticationHandler);
 * routerBuilder.get("/home/:user/**", bodyParser);
 * routerBuilder.get("/home/:user/**", userReadHandler);
 * </code>
 * <br/><br/>
 * 
 * If you need to add a handler before another handler that was already added then you can
 * use the methods that take an {@link Integer} priority.
 * 
 * <br/><br/>
 * Handlers are added with a priority of 10 if no priority is specified.
 * 
 * <code>
 * routerBuilder.get("/home/:user/**", logHandler, 0); // will place the logHandler before all other handlers for the route.
 * </code>
 * 
 * <br/><br/>
 * Call <code>routerBuilder.build()</code> to get an {@link IRouter} instance once all your routes have been registered.<br/>
 * The {@link IPluggableRouterBuilder} is capable of re-building and re-installing a {@link IRouter} at runtime.
 * 
 * @author tantaman
 *
 */
public interface IRouterBuilder {
	/**
	 * Register a handler that will be called to process HTTP GET requests on the given path.<br/><br/>
	 * 
	 * <code>routerBuilder.get("/home/:user/**", userReadHandlerFactory);</code>
	 * 
	 * @param path
	 * @param routeHandler
	 * @return
	 */
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandler);
	/**
	 * Register a handler that will be called to process HTTP PUT request on the given path.<br/><br/>
	 * 
	 * <code>routerBuilder.put("/home/:user/**", userReadHandlerFactory);</code>
	 * 
	 * @param path
	 * @param routeHandler
	 * @return
	 */
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder options(String route, IRouteHandlerFactory options);
	
	public IRouterBuilder get(String path, IRouteHandlerFactory routeHandler, int priority);
	public IRouterBuilder put(String path, IRouteHandlerFactory routeHandler, int priority);
	public IRouterBuilder post(String path, IRouteHandlerFactory routeHandler, int priority);
	public IRouterBuilder delete(String path, IRouteHandlerFactory routeHandler, int priority);
	
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandler);
	public IRouterBuilder all(String path, IRouteHandlerFactory routeHandler, int priority);
	
	/**
	 * Constructs and returns the {@link IRouter} implementation with the registered routes.
	 * @return
	 */
	public IRouter build();
}
