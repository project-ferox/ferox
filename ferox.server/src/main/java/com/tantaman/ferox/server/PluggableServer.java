package com.tantaman.ferox.server;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Map;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.ferox.api.server.IFeroxServer;
import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.api.server.IFeroxServerFactories;
import com.tantaman.ferox.api.server.IPluggableServer;

public class PluggableServer implements IPluggableServer {
	private volatile IFeroxFactories feroxFactories;
	private volatile IFeroxServerFactories serverFactories;
	private IFeroxServerBuilder serverBuilder;
	
	protected void setFeroxFactories(IFeroxFactories feroxFactories) {
		this.feroxFactories = feroxFactories;
	}
	
	protected void setServerFactories(IFeroxServerFactories serverFactories) {
		this.serverFactories = serverFactories;
	}
	
	protected void activate(Map<String, String> configuration) {
		serverBuilder = serverFactories.createServerBuilder();
		
		serverBuilder.use("decoder", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return (ChannelHandler) new HttpRequestDecoder();
			}
		});

		serverBuilder.use("encoder", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return (ChannelHandler) new HttpResponseEncoder();
			}
		});

		serverBuilder.use("chunkedWriter", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return new ChunkedWriteHandler();
			}
		});
	}
	
	public void use(IChannelHandlerFactory handlerFactory) {
		serverBuilder.use(null, handlerFactory);
	}
	
	public void listen(int port, boolean ssl) {
		serverBuilder.port(port);
		serverBuilder.ssl(ssl);
		
		IPluggableRouterBuilder pluggableRouterBuilder = feroxFactories.createPluggableRouterBuilder(null);
		serverBuilder.use("ferox", feroxFactories.createPluggableFeroxChannelHandlerFactory(pluggableRouterBuilder));
		
		IFeroxServer server = serverBuilder.build();
		Thread t = new Thread(server);
		t.setContextClassLoader(this.getClass().getClassLoader());
		t.start();
	}
}
