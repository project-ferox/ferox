package com.tantaman.ferox.api;

import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;

public interface IFeroxFactories {
	public IRouterBuilder createRouterBuilder();
	
	public IChannelHandlerFactory createFeroxChannelHandlerFactory(IRouter router);
}
