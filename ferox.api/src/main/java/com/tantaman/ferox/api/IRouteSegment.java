package com.tantaman.ferox.api;

import java.util.List;
import java.util.Map;


public interface IRouteSegment {
	public enum Type {
		WILD,
		STANDARD,
		REGEX,
		CATCHALL,
		ROOT;
	}
	
	Type type();

	boolean isLeaf();

	boolean matches(String piece);

	IRoute getOwner();

	void extract(String string, Map<String, String> urlParameters, List<String> splats);

}
