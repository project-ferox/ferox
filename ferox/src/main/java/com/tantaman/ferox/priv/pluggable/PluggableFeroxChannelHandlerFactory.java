package com.tantaman.ferox.priv.pluggable;

import io.netty.channel.ChannelHandler;

import com.tantaman.ferox.Ferox;
import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;

public class PluggableFeroxChannelHandlerFactory implements IPluggableChannelHandlerFactory {
	private static final IRouter EMPTY_ROUTER = new IRouter() {
		@Override
		public IRoute lookup(String method, String route) {
			return null;
		}
	};
	
	private final IPluggableRouterBuilder routerBuilder;
	private volatile IRouter router;
	
	public PluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder) {
		this.routerBuilder = routerBuilder;
	}
	
	@Override
	public ChannelHandler create() {
		if (router != null)
			return new Ferox(router);
		else
			return new Ferox(EMPTY_ROUTER);
	}

	@Override
	public String getIdentifier() {
		return null;
	}
}
