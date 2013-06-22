package com.tantaman.ferox;

import io.netty.channel.ChannelHandler;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRouter;

public class FeroxChannelHandlerFactory implements IChannelHandlerFactory {
	private final IRouter router;
	
	public FeroxChannelHandlerFactory(IRouter router) {
		this.router = router;
	}
	
	@Override
	public ChannelHandler create() {
		return new Ferox(router);
	}

}
