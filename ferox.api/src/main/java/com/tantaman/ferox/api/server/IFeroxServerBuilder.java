package com.tantaman.ferox.api.server;

import com.tantaman.ferox.api.IChannelHandlerFactory;

public interface IFeroxServerBuilder {
	public IFeroxServerBuilder port(int port);
	public IFeroxServerBuilder ssl(boolean useSsl);
	public IFeroxServerBuilder useFerox(final IChannelHandlerFactory ferox);
	public IFeroxServerBuilder use(String identifier, IChannelHandlerFactory factory);
	
	public IFeroxServer build();
}
