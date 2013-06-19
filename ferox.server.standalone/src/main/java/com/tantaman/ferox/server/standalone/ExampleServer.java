package com.tantaman.ferox.server.standalone;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.stream.ChunkedWriteHandler;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

import com.tantaman.ferox.api.IChannelHandlerFactory;
import com.tantaman.ferox.api.IFeroxFactories;
import com.tantaman.ferox.api.IFeroxServer;
import com.tantaman.ferox.api.IFeroxServerBuilder;
import com.tantaman.ferox.api.IFeroxServerFactories;
import com.tantaman.ferox.api.IHttpContent;
import com.tantaman.ferox.api.IHttpRequest;
import com.tantaman.ferox.api.IRequestChainer;
import com.tantaman.ferox.api.IResponse;
import com.tantaman.ferox.api.IRouteHandler;
import com.tantaman.ferox.api.IRouteHandlerFactory;
import com.tantaman.ferox.api.IRouterBuilder;

@Component(immediate=true)
public class ExampleServer {
	private IFeroxFactories feroxFactories;
	private IFeroxServerFactories serverFactories;
	
	@Reference
	public void setFeroxFactories(IFeroxFactories feroxFactories) {
		this.feroxFactories = feroxFactories;
	}
	
	@Reference
	public void setServerFactories(IFeroxServerFactories serverFactories) {
		this.serverFactories = serverFactories;
	}

	@Activate
	public void activate(ComponentContext context) {
		System.out.println("ACTIVATED");
		IFeroxServerBuilder b = serverFactories.createServerBuilder();

		b.port(8082);
		b.use("decoder", new IChannelHandlerFactory() {

			@Override
			public ChannelHandler create() {
				return new HttpRequestDecoder();
			}
		});

		b.use("encoder", new IChannelHandlerFactory() {

			@Override
			public ChannelHandler create() {
				return new HttpResponseEncoder();
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
				return new IRouteHandler() {
					@Override
					public void content(IHttpContent content,
							IResponse response, IRequestChainer next) {
						if (content.isLast()) {
							response.send("User: " + content.getUrlParam("user") + " id: " + content.getUrlParam("id"), HttpResponseStatus.OK);
						}
					}
					
					@Override
					public void request(IHttpRequest request,
							IResponse response, IRequestChainer next) {
					}
				};
			}
		});
		
		routerBuilder.get("/**", new IRouteHandlerFactory() {
			
			@Override
			public IRouteHandler create() {
				return new IRouteHandler() {
					
					@Override
					public void request(IHttpRequest request, IResponse response,
							IRequestChainer next) {
					}
					
					@Override
					public void content(IHttpContent content, IResponse response,
							IRequestChainer next) {
						if (content.isLast())
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
	
	@Deactivate
	public void deactivate(ComponentContext context) {
		
	}
}
