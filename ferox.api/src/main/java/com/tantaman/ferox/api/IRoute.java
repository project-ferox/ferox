package com.tantaman.ferox.api;

import java.util.Collection;

public interface IRoute extends Iterable<IRouteSegment> {
	public Collection<IRouteHandlerFactory> getHandlers();
}
