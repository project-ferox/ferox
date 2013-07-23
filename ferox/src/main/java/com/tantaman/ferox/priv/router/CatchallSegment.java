package com.tantaman.ferox.priv.router;

/**
 * Matches the catchall pattern.
 * @author tantaman
 *
 */
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
