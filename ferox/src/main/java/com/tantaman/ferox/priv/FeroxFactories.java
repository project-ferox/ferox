package com.tantaman.ferox.priv;

import com.tantaman.ferox.FeroxChannelHandlerFactory;
import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilderFactory;
import com.tantaman.ferox.pluggable.PluggableFeroxChannelHandlerFactory;
import com.tantaman.lo4j.Lo.Fn;

public class FeroxFactories implements IFeroxFactories {
	private volatile IPluggableRouterBuilderFactory pluggableRouterBuilderFactory;
	
	public void setPluggableRouterBuilderFactory(IPluggableRouterBuilderFactory factory) {
		pluggableRouterBuilderFactory = factory;
	}
	
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
	public IPluggableRouterBuilder createPluggableRouterBuilder(Fn<Boolean, IRouteInitializer> initializerFilter) {
		return pluggableRouterBuilderFactory.createRouterBuilder(initializerFilter);
	}

	@Override
	public IPluggableChannelHandlerFactory createPluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder) {
		return new PluggableFeroxChannelHandlerFactory(routerBuilder);
	}
}
