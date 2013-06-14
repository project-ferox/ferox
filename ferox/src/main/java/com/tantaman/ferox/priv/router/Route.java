package com.tantaman.ferox.priv.router;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tantaman.ferox.api.IRoute;
import com.tantaman.ferox.api.IRouteHandlerFactory;
import com.tantaman.ferox.api.IRouteSegment;

public class Route implements Iterable<IRouteSegment>, IRoute {
	private final List<IRouteSegment> segments;
	private final List<IRouteHandlerFactory> handlers;
	private final String method;
	
	public Route(String method, String path) {
		this.method = method;
		segments = new LinkedList<>();
		handlers = new LinkedList<>();
		
		String [] parts = path.split("/");
		if (parts[0].equals("")) {
			String [] newParts = new String[parts.length-1];
			System.arraycopy(parts, 1, newParts, 0, parts.length-1);
			
			parts = newParts;
		}
		
		segments.add(RouteSegment.create(method, parts.length == 0, this));
		int i = 0;
		for (String part : parts) {
			RouteSegment segment = RouteSegment.create(part, i == parts.length - 1, this);
			segments.add(segment);
			++i;
		}
	}
	
	public String getMethod() {
		return method;
	}
	
	@Override
	public Collection<IRouteHandlerFactory> getHandlers() {
		return handlers;
	}
	
	@Override
	public Iterator<IRouteSegment> iterator() {
		return segments.iterator();
	}

	public void addHandler(IRouteHandlerFactory routeHandler) {
		handlers.add(routeHandler);
	}
	
	public void addHandlers(IRouteHandlerFactory [] routeHandlers) {
		for (IRouteHandlerFactory rh : routeHandlers) {
			addHandler(rh);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (IRouteSegment s : this) {
			result.append(s);
		}
		
		return result.toString();
	}
}
