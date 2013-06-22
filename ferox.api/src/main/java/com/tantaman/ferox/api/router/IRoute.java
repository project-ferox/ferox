package com.tantaman.ferox.api.router;

import java.util.Collection;

public interface IRoute extends Iterable<IRouteSegment> {
	public Collection<IRouteHandlerFactory> getHandlers();
}
