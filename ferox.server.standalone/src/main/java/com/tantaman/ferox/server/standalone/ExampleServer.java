package com.tantaman.ferox.server.standalone;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.osgi.service.component.ComponentContext;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.api.request_response.IRequestChainer;
import com.tantaman.ferox.api.request_response.IResponse;
import com.tantaman.ferox.api.router.IRouteHandler;
import com.tantaman.ferox.api.router.IRouteHandlerFactory;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.RouteHandlerAdapter;
import com.tantaman.ferox.api.server.IFeroxServer;
import com.tantaman.ferox.api.server.IFeroxServerBuilder;
import com.tantaman.ferox.api.server.IFeroxServerFactories;

public class ExampleServer {
	private IFeroxFactories feroxFactories;
	private IFeroxServerFactories serverFactories;
	
	public void setFeroxFactories(IFeroxFactories feroxFactories) {
		this.feroxFactories = feroxFactories;
	}
	
	public void setServerFactories(IFeroxServerFactories serverFactories) {
		this.serverFactories = serverFactories;
	}

	public void activate(ComponentContext context) {
		System.out.println("ACTIVATED");
		IFeroxServerBuilder b = serverFactories.createServerBuilder();

		b.port(8082);
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

		// now add Ferox
		IRouterBuilder routerBuilder = feroxFactories.createRouterBuilder();
		routerBuilder.get("/:user/:id", new IRouteHandlerFactory() {
			@Override
			public IRouteHandler create() {
				return new RouteHandlerAdapter() {
					@Override
					public void lastContent(IHttpContent content,
							IResponse response, IRequestChainer next) {
						response.send("User: " + content.getUrlParam("user") + " id: " + content.getUrlParam("id"), HttpResponseStatus.OK);
					}
				};
			}
		});
		
		routerBuilder.get("/**", new IRouteHandlerFactory() {
			
			@Override
			public IRouteHandler create() {
				return new RouteHandlerAdapter() {
					@Override
					public void lastContent(IHttpContent content, IResponse response,
							IRequestChainer next) {
						response.send("Not Found", HttpResponseStatus.NOT_FOUND);
					}
				};
			}
		});
		
		b.use("ferox", feroxFactories.createFeroxChannelHandlerFactory(routerBuilder.build()));
		
		IFeroxServer server = b.build();
		try {
			server.runInCurrentThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void deactivate(ComponentContext context) {
		
	}
}
