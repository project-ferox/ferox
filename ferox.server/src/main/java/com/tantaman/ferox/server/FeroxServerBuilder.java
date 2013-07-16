package com.tantaman.ferox.server;

import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslHandler;

import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLEngine;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.server.IFeroxServer;
import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.server.priv.FeroxChannelInitializer;
import com.tantaman.ferox.server.priv.SslConfig;
import com.tantaman.ferox.util.IPair;
import com.tantaman.ferox.util.Pair;


public class FeroxServerBuilder implements IFeroxServerBuilder {
	private int port;
	private final List<IPair<String, IChannelHandlerFactory>> handlerFactories;
	
	public FeroxServerBuilder() {
		handlerFactories = new LinkedList<>();
	}
	
	@Override
	public IFeroxServerBuilder ssl(boolean useSsl) {
		if (useSsl) {
			handlerFactories.add(0, new Pair<String, IChannelHandlerFactory>("ssl", new IChannelHandlerFactory() {
				
				@Override
				public ChannelHandler create() {
					final SSLEngine engine = SslConfig.context().createSSLEngine();
			        engine.setUseClientMode(false);
					return new SslHandler(engine);
				}
			}));
		}
		
		return this;
	}

	public FeroxServerBuilder port(int port) {
		this.port = port;
		return this;
	}
	
	// So we can decide where in the pipeline to put it.
	public FeroxServerBuilder useFerox(final IChannelHandlerFactory ferox) {
		use("ferox", ferox);
		
		return this;
	}
	
	public FeroxServerBuilder use(String identifier, IChannelHandlerFactory factory) {
		handlerFactories.add(new Pair<String, IChannelHandlerFactory>(identifier, factory));
		return this;
	}
	
	public IFeroxServer build() {
		FeroxChannelInitializer fci = new FeroxChannelInitializer(handlerFactories);
		return new FeroxServer(port, fci);
	}
}
