package com.tantaman.ferox.priv.router;

import junit.framework.TestCase;

import com.tantaman.ferox.RouterBuilder;
import com.tantaman.ferox.api.IRouteHandler;
import com.tantaman.ferox.api.IRouteHandlerFactory;
import com.tantaman.ferox.api.IRouter;
import com.tantaman.ferox.util.HTTPMethods;
import com.tantaman.lo4j._;

public class RouterBuilderTest extends TestCase {
	private static class EmptyFactory implements IRouteHandlerFactory {
		@Override
		public IRouteHandler create() {
			return null;
		}
	}
	
	public void testStandard() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory onetwo = new EmptyFactory();
		rb.get("/one/two", onetwo);
		EmptyFactory twothree = new EmptyFactory();
		rb.put("/two/three", twothree);
		EmptyFactory onethree = new EmptyFactory();
		rb.get("/one/three", onethree);
		EmptyFactory one = new EmptyFactory();
		rb.get("/one", one);
		
		IRouter router = rb.build();
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/one/two").getHandlers()) == onetwo);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "one/two/").getHandlers()) == onetwo);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/one/").getHandlers()) == one);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/one").getHandlers()) == one);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "one/").getHandlers()) == one);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "one").getHandlers()) == one);
		assertTrue(router.lookup(HTTPMethods.GET, "/one/five") == null);
		
		rb = new RouterBuilder();
		EmptyFactory first = new EmptyFactory();
		rb.get("/a/b/c", first);
		EmptyFactory second = new EmptyFactory();
		rb.get("/a/b/c", second);
		router = rb.build();
		
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/a/b/c").getHandlers()) == first);
		assertTrue(_.last(router.lookup(HTTPMethods.GET, "/a/b/c").getHandlers()) == second);
	}

	public void testCatchall() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory trulyanything = new EmptyFactory();
		rb.get("**", trulyanything);
		EmptyFactory usersanything = new EmptyFactory();
		rb.get("/users/**", usersanything);
		
		IRouter router = rb.build();
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/some/stuff").getHandlers()) == trulyanything);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/users/u").getHandlers()) == usersanything);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/users").getHandlers()) == usersanything);
		assertTrue(_.first(router.lookup(HTTPMethods.GET, "/").getHandlers()) == trulyanything);
		
		rb = new RouterBuilder();
		rb.get("/one/**/two", new EmptyFactory());
		
		boolean except = false;
		try {
			rb.build();
		} catch (IllegalStateException e) {
			except = true;
		}
		assertTrue(except);
	}
	
	
	public void testRegex() {
		
	}
	
	public void testLateFailNoCatchall() {
		
	}
	
	public void testLateFailWithCatchall() {
		
	}
	
	public void testMultipleWildsAtSamePosition() {
		
	}
	
	public void testMultipleRegexes() {
		
	}
	
	public void testMultipleCatchalls() {
		
	}
	
	public void testAmbiguousCatcahll() {
		
	}
	
	public void testAmbiguousWild() {
		
	}
}
