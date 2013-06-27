package com.tantaman.ferox.pluggable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IRouteHandlerFactoryTuple;

public class PluggableRouterBuilder extends RouterBuilder {
	// TODO: debounce the `routes staged` events.
	// TODO: thread confinement?  What threads do service registrations come in on?
	private static final ExecutorService builderThread = Executors.newFixedThreadPool(1);
	
	public PluggableRouterBuilder() {
		// listen for registrations of IRouteHandlerFactoryTuple and reconstruct
		// the router.
	}
	
	private void tupleRegistered(final IRouteHandlerFactoryTuple tuple) {
		builderThread.execute(new Runnable() {
			@Override
			public void run() {
				registerRoute(tuple);
			}
		});
	}
	
	private void registerRoute(IRouteHandlerFactoryTuple tuple) {
		
	}
	
	private void routesStaged() {
		
	}
}
