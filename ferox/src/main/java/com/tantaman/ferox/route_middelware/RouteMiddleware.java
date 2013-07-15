package com.tantaman.ferox.route_middelware;

import com.tantaman.ferox.api.router.IRouteHandler;
import com.tantaman.ferox.api.router.IRouteHandlerFactory;

public class RouteMiddleware {
	public static IRouteHandlerFactory staticContent(final String path) {
		return new IRouteHandlerFactory() {
			@Override
			public IRouteHandler create() {
				return new StaticHandler(path);
			}
		};
	}
	
	public static IRouteHandlerFactory bodyParser() {
		return new IRouteHandlerFactory() {
			
			@Override
			public IRouteHandler create() {
				return new BodyParserHandler();
			}
		};
	}
}
