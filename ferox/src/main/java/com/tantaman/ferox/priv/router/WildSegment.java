package com.tantaman.ferox.priv.router;

import java.util.List;
import java.util.Map;

public class WildSegment extends RouteSegment {
	private final String name;
	
	public WildSegment(String wild, boolean leaf, Route owner) {
		super(Type.WILD, wild, leaf, owner);
		name = wild.substring(1);
	}
	
	@Override
	public boolean matches(String piece) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void extract(String string, Map<String, String> urlParameters,
			List<String> splats) {
		urlParameters.put(name, string);
	}
}
