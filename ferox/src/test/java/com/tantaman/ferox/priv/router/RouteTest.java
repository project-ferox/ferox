package com.tantaman.ferox.priv.router;

import junit.framework.TestCase;


public class RouteTest extends TestCase {

	public void testIt() {
		Route r = new Route("get", "/go/to/this");
		
		assertEquals(
				"{:type STANDARD :name get :raw get}/{:type STANDARD :name go :raw go}/{:type STANDARD :name to :raw to}/{:type STANDARD :name this :raw this}/",
				r.toString());
		
		r = new Route("get", "/go/:to/:that");
		
		assertEquals(
				"{:type STANDARD :name get :raw get}/{:type STANDARD :name go :raw go}/{:type WILD :name to :raw :to}/{:type WILD :name that :raw :that}/",
				r.toString());
		
		r = new Route("put", "/*/:this:[a-z]+");
		
		assertEquals("{:type STANDARD :name put :raw put}/{:type WILD :name splat :raw *}/{:type REGEX :name this :raw :this:[a-z]+}/",
				r.toString());
		
		r = new Route("delete", "/**");
		
		assertEquals("{:type STANDARD :name delete :raw delete}/{:type CATCHALL :name catchall :raw **}/", r.toString());
	}

}
