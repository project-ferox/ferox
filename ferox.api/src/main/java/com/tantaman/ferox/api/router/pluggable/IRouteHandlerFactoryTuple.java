package com.tantaman.ferox.api.router.pluggable;

import com.tantaman.ferox.api.router.IRouteHandlerFactory;

public interface IRouteHandlerFactoryTuple {
	public String getMethod();
	public String getPath();
	public IRouteHandlerFactory getHandlerFactory();
}
