package com.tantaman.ferox.api;

import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.lo4j.Lo.Fn;

public interface IFeroxFactories {
	public IRouterBuilder createRouterBuilder();
	
	public IChannelHandlerFactory createFeroxChannelHandlerFactory(IRouter router);
	public IPluggableRouterBuilder createPluggableRouterBuilder(Fn<Boolean, IRouteInitializer> initializerFilter);
	public IPluggableChannelHandlerFactory createPluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder);
}
