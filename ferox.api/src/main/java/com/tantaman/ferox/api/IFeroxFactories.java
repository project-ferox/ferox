package com.tantaman.ferox.api;

public interface IFeroxFactories {
	public IRouterBuilder createRouterBuilder();
	
	public IChannelHandlerFactory createFeroxChannelHandlerFactory(IRouter router);
}
