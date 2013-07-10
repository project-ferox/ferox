package com.tantaman.ferox.api.server;

import com.tantaman.ferox.api.IChannelHandlerFactory;

public interface IPluggableServer {
	public void listen(int port, boolean ssl);
	public void use(IChannelHandlerFactory handler);
}
