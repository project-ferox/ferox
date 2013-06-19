package com.tantaman.ferox.priv;

import com.tantaman.ferox.FeroxChannelHandlerFactory;
import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.IRouter;
import com.tantaman.ferox.api.IRouterBuilder;

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

}
