package com.tantaman.ferox.api.router;

import java.util.Collection;

import com.tantaman.ferox.util.IPair;

public interface IRoute extends Iterable<IRouteSegment> {
	public Collection<IPair<Integer, IRouteHandlerFactory>> getHandlers();
}
