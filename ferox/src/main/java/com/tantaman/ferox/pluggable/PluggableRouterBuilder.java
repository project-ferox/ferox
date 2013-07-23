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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.api.router.IRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.lo4j.Lo;
import com.tantaman.lo4j.NamedThreadFactory;

/**
 * The {@link PluggableRouterBuilder} listens for service events (via OSGi declarative services)
 * relating to {@link IRouteInitializer}s.
 * 
 * Whenever a {@link IRouteInitializer} comes online, or goes offline, the routing table is rebuilt.
 * 
 * Subsequent connections by a user to the Ferox server will use the new routes while
 * currently established connections will continue to use the old routes.
 * 
 * Instances of the {@link PluggableRouterBuilder} are managed by the {@link PluggableRouterBuilderFactory}.
 * The {@link PluggableRouterBuilderFactory} is the class that listens for the actual service events
 * and forwards them on to any instances of the {@link PluggableRouterBuilder} that it may be managing.
 * 
 * @author tantaman
 *
 */
public class PluggableRouterBuilder implements IPluggableRouterBuilder {
	private static final Logger log = LoggerFactory.getLogger(PluggableRouterBuilder.class);
	private static final ScheduledExecutorService builderThread = Executors.newScheduledThreadPool(1, new NamedThreadFactory("PluggableRouterBuilder"));
	
	private IRouterBuilder routerBuilder;
	private final Set<Listener> listeners = new CopyOnWriteArraySet<>();
	private final Set<IRouteInitializer> routeInitializers;
	private final Lo.Fn<Void, Void> debouncedRebuild = Lo.debounce(new Lo.Fn<Void, Void>() {
		public Void f(Void p) {
			rebuildRoutes();
			return null;
		};
	}, 500, TimeUnit.MILLISECONDS, builderThread);
	
	private static final Comparator<IRouteInitializer> INITIALIZER_COMPARATOR = new Comparator<IRouteInitializer>() {
		@Override
		public int compare(IRouteInitializer o1, IRouteInitializer o2) {
			return o1.getPriority() - o2.getPriority();
		}
	};
	
	PluggableRouterBuilder() {
		routeInitializers = new HashSet<>();
	}
	
	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void addRouteInitializer(final IRouteInitializer routeInitializer) {
		builderThread.execute(new Runnable() {
			@Override
			public void run() {
				initializerAdded(routeInitializer);
			}
		});
	}
	
	public void removeRouteInitializer(final IRouteInitializer routeInitializer) {
		log.debug("Removed route initializer");
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
		
		for (IRouteInitializer initializer : sortedInitializers) {
			initializer.addRoutes(routerBuilder);
		}
		
		notifyStaged();
		
		IRouter router = routerBuilder.build();
		notifyRebuilt(router);
	}
}
