package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouteInitializer;
import com.tantaman.lo4j.Lo;

/**
 * Service interface for creating managed {@link IPluggableRouterBuilder}s.
 * 
 * @author tantaman
 *
 */
public interface IPluggableRouterBuilderFactory {
	public IPluggableRouterBuilder createRouterBuilder(Lo.Fn<Boolean, IRouteInitializer> initializerFilter);
}
