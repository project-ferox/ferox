package com.tantaman.ferox.server.pluggable;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.osgi.service.component.ComponentContext;

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
	
	public void activate(ComponentContext context) {
		IFeroxServerBuilder b = serverFactories.createServerBuilder();
		
		b.port(8082);
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
		
		// create the router builder via the configuration admin...
		IPluggableRouterBuilder pluggableRouterBuilder = feroxFactories.createPluggableRouterBuilder();
		b.use("ferox", feroxFactories.createPluggableFeroxChannelHandlerFactory(pluggableRouterBuilder));
		
		IFeroxServer server = b.build();
		try {
			server.runInCurrentThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
