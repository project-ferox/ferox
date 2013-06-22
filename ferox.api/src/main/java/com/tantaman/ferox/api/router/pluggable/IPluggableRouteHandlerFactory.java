package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouteHandlerFactory;

public interface IPluggableRouteHandlerFactory extends IRouteHandlerFactory {
	public String getIdentifier();
}
