package com.tantaman.ferox.server.pluggable;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Validate;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.server.IFeroxServer;
import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.api.server.IFeroxServerFactories;

@Component(immediate=true)
public class PluggableServer {
	private volatile IFeroxFactories feroxFactories;
	private volatile IFeroxServerFactories serverFactories;
	
	@Bind
	public void bindFeroxFactories(IFeroxFactories feroxFactories) {
		System.out.println("Bound factories...");
		this.feroxFactories = feroxFactories;
	}
	
	@Bind
	public void bindServerFactories(IFeroxServerFactories serverFactories) {
		this.serverFactories = serverFactories;
	}
	
	@Validate
	public void activate() {
		System.out.println("PLUGGABLE SERVER ACTIVATED");
		IFeroxServerBuilder b = serverFactories.createServerBuilder();
		
		// TODO: get this stuff from configuration admin?  env vars?  java properties?
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
		
		b.use("ferox", feroxFactories.createPluggableFeroxChannelHandlerFactory(feroxFactories.createPluggableRouterBuilder()));
		
		IFeroxServer server = b.build();
		try {
			server.runInCurrentThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
