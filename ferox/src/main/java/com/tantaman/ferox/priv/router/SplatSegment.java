package com.tantaman.ferox.priv.router;

import java.util.List;
import java.util.Map;


public class SplatSegment extends RouteSegment {
	
	public SplatSegment(boolean leaf, Route owner) {
		super(Type.WILD, "*", leaf, owner);
	}
	
	@Override
	public String getName() {
		return "splat";
	}
	
	@Override
	public boolean matches(String piece) {
		return true;
	}
	
	@Override
	public void extract(String string, Map<String, String> urlParameters,
			List<String> splats) {
		splats.add(string);
	}
}
