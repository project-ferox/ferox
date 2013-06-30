package com.tantaman.ferox.pluggable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilderFactory;
import com.tantaman.ferox.util.Pair;
import com.tantaman.lo4j.Lo.Fn;

public class PluggableRouterBuilderFactory implements IPluggableRouterBuilderFactory {
	private final List<Pair<Fn<Boolean, IRouteInitializer>, WeakReference<IPluggableRouterBuilder>>> builders
		= new CopyOnWriteArrayList<>();
	
	@Override
	public IPluggableRouterBuilder createRouterBuilder(
			Fn<Boolean, IRouteInitializer> initializerFilter) {
		IPluggableRouterBuilder builder = new PluggableRouterBuilder();
		Pair<Fn<Boolean, IRouteInitializer>, WeakReference<IPluggableRouterBuilder>> reference
			= new Pair<>(initializerFilter, new WeakReference<IPluggableRouterBuilder>(builder));
		builders.add(reference);
		
		return builder;
	}

	public void addRouteInitializer(IRouteInitializer routeInitializer) {
		handleRouteInitializerEvent(routeInitializer, true);
	}
	
	public void removeRouteInitializer(IRouteInitializer routeInitializer) {
		handleRouteInitializerEvent(routeInitializer, false);
	}
	
	private void handleRouteInitializerEvent(IRouteInitializer routeInitializer, boolean add) {
		for (Pair<Fn<Boolean, IRouteInitializer>, WeakReference<IPluggableRouterBuilder>> pair : builders) {
			WeakReference<IPluggableRouterBuilder> ref = pair.getSecond();
			IPluggableRouterBuilder b = ref.get();
			if (b == null) {
				builders.remove(pair);
			} else if (pair.getFirst() == null || pair.getFirst().f(routeInitializer)) {
				if (add)
					b.addRouteInitializer(routeInitializer);
				else
					b.removeRouteInitializer(routeInitializer);
			}
		}
	}
}
