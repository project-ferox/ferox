package com.tantaman.ferox.pluggable;

import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilder;
import com.tantaman.ferox.api.router.pluggable.IPluggableRouterBuilderFactory;
import com.tantaman.lo4j.Lo.Fn;

public class PluggableRouterBuilderFactory implements IPluggableRouterBuilderFactory {

	@Override
	public IPluggableRouterBuilder createRouterBuilder(
			Fn<Boolean, IRouteInitializer> initializerFilter) {
		return null;
	}

}
