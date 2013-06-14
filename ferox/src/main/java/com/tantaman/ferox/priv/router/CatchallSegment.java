package com.tantaman.ferox.priv.router;

public class CatchallSegment extends RouteSegment {

	public CatchallSegment(boolean leaf, Route owner) {
		super(Type.CATCHALL, "**", leaf, owner);
	}
	
	@Override
	public String getName() {
		return "catchall";
	}
	
	@Override
	public boolean matches(String piece) {
		return true;
	}
}
