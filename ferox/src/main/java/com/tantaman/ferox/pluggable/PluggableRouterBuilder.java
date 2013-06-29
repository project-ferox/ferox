package com.tantaman.ferox.pluggable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.lo4j._;

public class PluggableRouterBuilder implements IPluggableRouterBuilder {
	// TODO: debounce the `routes staged` events.
	// TODO: thread confinement?  What threads do service registrations come in on?
	private static final ScheduledExecutorService builderThread = Executors.newScheduledThreadPool(1);
	
	private IRouterBuilder routerBuilder;
	private final Set<Listener> listeners = new CopyOnWriteArraySet<>();
	private final Set<IRouteInitializer> routeInitializers;
	private final _.Fn<Void, Void> debouncedRebuild = _.debounce(new _.Fn<Void, Void>() {
		public Void f(Void p) {
			rebuildRoutes();
			return null;
		};
	}, 250, TimeUnit.MILLISECONDS, builderThread);
	
	private static final Comparator<IRouteInitializer> INITIALIZER_COMPARATOR = new Comparator<IRouteInitializer>() {
		@Override
		public int compare(IRouteInitializer o1, IRouteInitializer o2) {
			return o1.getPriority() - o2.getPriority();
		}
	};
	
	public PluggableRouterBuilder() {
		routeInitializers = new HashSet<>();
	}
	
	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void bindRouteInitializer(final IRouteInitializer routeInitializer) {
		builderThread.execute(new Runnable() {
			@Override
			public void run() {
				initializerAdded(routeInitializer);
			}
		});
	}
	
	public void unbindRouteInitializer(final IRouteInitializer routeInitializer) {
		builderThread.execute(new Runnable() {
			@Override
			public void run() {
				initializerRemoved(routeInitializer);
			}
		});
	}
	
	private void initializerAdded(IRouteInitializer routeInitializer) {
		routeInitializers.add(routeInitializer);
		debouncedRebuild.f(null);
	}
	
	private void initializerRemoved(IRouteInitializer routeInitializer) {
		routeInitializers.remove(routeInitializer);
		debouncedRebuild.f(null);
	}
	
	private void notifyStaged() {
		for (Listener l : listeners) {
			l.newRoutesStaged(routerBuilder);
		}
	}
	
	private void notifyRebuilt(IRouter router) {
		for (Listener l : listeners) {
			l.routesRebuilt(router);
		}
	}
	
	private void rebuildRoutes() {
		routerBuilder = new RouterBuilder();
		List<IRouteInitializer> sortedInitializers = new ArrayList<>(routeInitializers);
		
		Collections.sort(sortedInitializers, INITIALIZER_COMPARATOR);
		
		notifyStaged();
		IRouter router = routerBuilder.build();
		notifyRebuilt(router);
	}
}
