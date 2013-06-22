package com.tantaman.ferox.priv;

import com.tantaman.ferox.FeroxChannelHandlerFactory;
import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.ferox.priv.pluggable.PluggableFeroxChannelHandlerFactory;

public class FeroxFactories implements IFeroxFactories {
	@Override
	public IRouterBuilder createRouterBuilder() {
		return new RouterBuilder();
	}

	@Override
	public IChannelHandlerFactory createFeroxChannelHandlerFactory(
			IRouter router) {
		return new FeroxChannelHandlerFactory(router);
	}
	
	@Override
	public IPluggableRouterBuilder createPluggableRouterBuilder() {
		return null;
	}

	@Override
	public IPluggableChannelHandlerFactory createPluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder) {
		return new PluggableFeroxChannelHandlerFactory(routerBuilder);
	}
}
