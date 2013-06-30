package com.tantaman.ferox.server.standalone;

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

public class PluggableServer {
	private volatile IFeroxFactories feroxFactories;
	private volatile IFeroxServerFactories serverFactories;
	
	public void setFeroxFactories(IFeroxFactories feroxFactories) {
		this.feroxFactories = feroxFactories;
	}
	
	public void setServerFactories(IFeroxServerFactories serverFactories) {
		this.serverFactories = serverFactories;
	}
	
	public void activate(Map<String, String> configuration) {
		IFeroxServerBuilder b = serverFactories.createServerBuilder();
		
		int port = 8080;
		try {
			port = Integer.parseInt(configuration.get("port"));
		} catch (Exception e) {
			System.err.println("Could not find port in configuration.  Using " + port);
		}
		b.port(port);
		
		// TODO: the pipeline should probably be pluggable too.
		// Should we just support the pluggable approach and get rid of the non pluggable approach?
		b.use("decoder", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return (ChannelHandler) new HttpRequestDecoder();
			}
		});

		b.use("encoder", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return (ChannelHandler) new HttpResponseEncoder();
			}
		});

		b.use("chunkedWriter", new IChannelHandlerFactory() {
			@Override
			public ChannelHandler create() {
				return new ChunkedWriteHandler();
			}
		});
		
		IPluggableRouterBuilder pluggableRouterBuilder = feroxFactories.createPluggableRouterBuilder(null);
		b.use("ferox", feroxFactories.createPluggableFeroxChannelHandlerFactory(pluggableRouterBuilder));
		
		IFeroxServer server = b.build();
		Thread t = new Thread(server);
		t.setContextClassLoader(this.getClass().getClassLoader());
		t.start();
	}
}
