package com.tantaman.ferox.priv.router;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matches a regular expression.
 * @author tantaman
 *
 */
public class RegexSegment extends RouteSegment {
	private final Pattern reg;
	private final String name;
	
	public RegexSegment(String piece, boolean leaf, Route owner) {
		super(Type.REGEX, piece, leaf, owner);
		
		String temp = piece.substring(1);
		
		int divider = temp.indexOf(':');
		name = temp.substring(0, divider);
		reg = Pattern.compile(temp.substring(divider+1));
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean matches(String piece) {
		Matcher m = reg.matcher(piece);
		return m.matches();
	}
	
	@Override
	public void extract(String string, Map<String, String> urlParameters,
			List<String> splats) {
		urlParameters.put(name, string);
	}
}
