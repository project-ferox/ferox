package com.tantaman.ferox.priv.router;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RouteParameters {
	private final Map<String, String> namedParameters;
	private final List<String> splats;
	private final List<String> captures;
	
	public RouteParameters() {
		namedParameters = new HashMap<>();
		splats = new LinkedList<>();
		captures = new LinkedList<>();
	}
	
	public void extractParameters(Route registeredRoute, String requestedRoute) {
		
	}

}
