package com.tantaman.ferox.pluggable;

import io.netty.channel.ChannelHandler;

import com.tantaman.ferox.Ferox;
import com.tantaman.ferox.api.pluggable.IPluggableChannelHandlerFactory;
import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;

/**
 * {@link Ferox} is added as a channel handler to the Netty pipeline.
 * 
 * As per Netty's design, every time a new channel is created new instances of the handlers
 * are created which is why Ferox is constructed via a factory.
 * 
 * The {@link PluggableFeroxChannelHandlerFactory} creates instances of Ferox that
 * use the {@link IPluggableRouterBuilder} to get its routes.
 * 
 * The {@link IPluggableRouterBuilder} automatically rebuilds routes as {@link IRouteInitializer}
 * services come and go into / out of existence.
 * 
 * @author tantaman
 *
 */
public class PluggableFeroxChannelHandlerFactory implements IPluggableChannelHandlerFactory {
	private static final IRouter EMPTY_ROUTER = new IRouter() {
		@Override
		public IRoute lookup(String method, String route) {
			return null;
		}
	};
	
	private final IPluggableRouterBuilder routerBuilder;
	private volatile IRouter router;
	
	public PluggableFeroxChannelHandlerFactory(IPluggableRouterBuilder routerBuilder) {
		this.routerBuilder = routerBuilder;
		
		this.routerBuilder.addListener(new IPluggableRouterBuilder.Listener() {
			@Override
			public void routesRebuilt(IRouter newRouter) {
				router = newRouter;
			}
			
			@Override
			public void newRoutesStaged(IRouterBuilder routerBuilder) {}
		});
	}
	
	@Override
	public ChannelHandler create() {
		if (router != null)
			return new Ferox(router);
		else
			return new Ferox(EMPTY_ROUTER);
	}

	@Override
	public String getIdentifier() {
		return "ferox";
	}
}
