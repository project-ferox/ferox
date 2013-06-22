package com.tantaman.ferox.api;

import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;

public interface IFeroxFactories {
	public IRouterBuilder createRouterBuilder();
	
	public IChannelHandlerFactory createFeroxChannelHandlerFactory(IRouter router);
	public IPluggableRouterBuilder createPluggableRouterBuilder();
	public IPluggableChannelHandlerFactory createPluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder);
}
