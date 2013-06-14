package com.tantaman.ferox.priv.router;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.IRouteSegment;


public class RouteSegment implements IRouteSegment {	
	private final String stringRep;
	private final Type type;
	private final Route owningRoute;
	private final boolean leaf;
	
	public RouteSegment(Type type, String stringRep, boolean leaf, Route owningRoute) {
		this.type = type;
		this.stringRep = stringRep;
		this.owningRoute = owningRoute;
		this.leaf = leaf;
	}
	
	public static RouteSegment create(String piece, boolean leaf, Route owner) {
		if (piece.charAt(0) == ':') {
			if (piece.lastIndexOf(':') != 0) 
				return new RegexSegment(piece, leaf, owner);
			
			return new WildSegment(piece, leaf, owner);
		}
		
		if (piece.equals("*"))
			return new SplatSegment(leaf, owner);
		
		if (piece.equals("**"))
			return new CatchallSegment(leaf, owner);
		
		return new RouteSegment(Type.STANDARD, piece, leaf, owner);
	}
	
	@Override
	public void extract(String string, Map<String, String> urlParameters,
			List<String> splats) {
		return;
	}
	
	public Type type() {
		return this.type;
	}

	public String getString() {
		return this.stringRep;
	}

	public String getName() {
		return this.stringRep;
	}

	// overridden by regexSegment, wildSegment, splatSegment and so on.
	public boolean matches(String piece) {
		return piece.equals(stringRep);
	}

	public boolean isLeaf() {
		return leaf;
	}

	public Route getOwner() {
		return owningRoute;
	}
	
	@Override
	public String toString() {
		return "{:type " + type() + " :name " + getName() + " :raw " + getString() + "}/";
	}
	
	@Override
	public int hashCode() {
		return stringRep.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			RouteSegment cast = (RouteSegment)obj;
			
			return cast.type == type && cast.stringRep.equals(stringRep);
		}
		
		return false;
	}
}
