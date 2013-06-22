package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouterBuilder;

public interface IPluggableRouterBuilder extends IRouterBuilder {
	public static interface Listener {
		public void newRoutesStaged();
		public void routesRebuilt();
	}
	
	public void addListener(Listener listener);
}
