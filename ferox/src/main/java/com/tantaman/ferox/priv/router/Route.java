package com.tantaman.ferox.priv.router;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.tantaman.ferox.api.router.IRoute;
import com.tantaman.ferox.api.router.IRouteHandlerFactory;
import com.tantaman.ferox.api.router.IRouteSegment;
import com.tantaman.ferox.util.IPair;

/**
 * A route is a list of segments (standard, splat (*), catchall (**), regex, wild (:var))
 * @author tantaman
 *
 */
public class Route implements Iterable<IRouteSegment>, IRoute {
	private final List<IRouteSegment> segments;
	private final List<IPair<Integer, IRouteHandlerFactory>> handlers;
	private final String method;
	
	private static final Comparator<IPair<Integer, IRouteHandlerFactory>> handlerComparator = 
	new Comparator<IPair<Integer,IRouteHandlerFactory>>() {
		@Override
		public int compare(IPair<Integer, IRouteHandlerFactory> o1,
				IPair<Integer, IRouteHandlerFactory> o2) {
			return o1.getFirst() - o2.getFirst();
		}
	};
	
	public Route(String method, String path) {
		this.method = method;
		segments = new LinkedList<>();
		handlers = new ArrayList<>();
		
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
	public Collection<IPair<Integer, IRouteHandlerFactory>> getHandlers() {
		return handlers;
	}
	
	@Override
	public Iterator<IRouteSegment> iterator() {
		return segments.iterator();
	}

	public void addHandler(IPair<Integer, IRouteHandlerFactory> routeHandler) {
		int index = Collections.binarySearch(handlers, routeHandler, handlerComparator);
		if (index < 0) {
			index = -1*index - 1;
		} else {
			// add it as the last route with the given priority.
			while (index < handlers.size() && handlers.get(index).getFirst().equals(routeHandler.getFirst()))
				++index;
		}
		handlers.add(index, routeHandler);
	}
	
	public void addHandlers(IPair<Integer, IRouteHandlerFactory> [] routeHandlers) {
		for (IPair<Integer, IRouteHandlerFactory> rh : routeHandlers) {
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
