package com.tantaman.ferox;


import org.junit.Test;

import static org.junit.Assert.*;

import com.tantaman.ferox.api.router.IRouteHandler;
import com.tantaman.ferox.api.router.IRouteHandlerFactory;
import com.tantaman.ferox.api.router.IRouter;
import com.tantaman.ferox.util.HTTPMethods;
import com.tantaman.lo4j.Lo;

public class RouterBuilderTest {
	private static class EmptyFactory implements IRouteHandlerFactory {
		@Override
		public IRouteHandler create() {
			return null;
		}
	}
	
	@Test
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
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/one/two").getHandlers()) == onetwo);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "one/two/").getHandlers()) == onetwo);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/one/").getHandlers()) == one);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/one").getHandlers()) == one);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "one/").getHandlers()) == one);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "one").getHandlers()) == one);
		assertTrue(router.lookup(HTTPMethods.GET, "/one/five") == null);
		
		rb = new RouterBuilder();
		EmptyFactory first = new EmptyFactory();
		rb.get("/a/b/c", first);
		EmptyFactory second = new EmptyFactory();
		rb.get("/a/b/c", second);
		router = rb.build();
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/a/b/c").getHandlers()) == first);
		assertTrue(Lo.last(router.lookup(HTTPMethods.GET, "/a/b/c").getHandlers()) == second);
	}

	@Test
	public void testCatchall() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory trulyanything = new EmptyFactory();
		rb.get("**", trulyanything);
		EmptyFactory usersanything = new EmptyFactory();
		rb.get("/users/**", usersanything);
		
		IRouter router = rb.build();
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/some/stuff").getHandlers()) == trulyanything);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/users/u").getHandlers()) == usersanything);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/users").getHandlers()) == usersanything);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/").getHandlers()) == trulyanything);
		
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
	
	@Test
	public void testRegex() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory regulusOne = new EmptyFactory();
		rb.get("/:some:regex+", regulusOne);
		
		EmptyFactory regTwo = new EmptyFactory();
		rb.get("/:some:reg+ex", regTwo);
		
		IRouter router = rb.build();
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/regex").getHandlers()) == regulusOne);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/regexxx").getHandlers()) == regulusOne);
		assertTrue(router.lookup(HTTPMethods.GET, "/rege") == null);
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/reggex").getHandlers()) == regTwo);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/regggex").getHandlers()) == regTwo);
		
		assertTrue(router.lookup(HTTPMethods.GET, "/regggexxx") == null);
	}
	
	@Test
	public void testWild() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory collection = new EmptyFactory();
		rb.post("/:collection/:id", collection);
		
		EmptyFactory oneWild = new EmptyFactory();
		rb.get("/:dir", oneWild);
		
		EmptyFactory oneLiteral = new EmptyFactory();
		rb.get("/literal", oneLiteral);
		
		EmptyFactory oneWildSecondHandler = new EmptyFactory();
		rb.get("/:dir", oneWildSecondHandler);
		
		EmptyFactory oneWildDifferentVerb = new EmptyFactory();
		rb.post("/:dir", oneWildDifferentVerb);
		
		EmptyFactory stuffThenWild = new EmptyFactory();
		rb.get("/a/loc/:here", stuffThenWild); 
		
		EmptyFactory wildThenStuff = new EmptyFactory();
		rb.get(":loc/to/be", wildThenStuff);
		
		EmptyFactory surround = new EmptyFactory();
		rb.get("/a/:thing/:here/ok", surround);
		
		IRouter router = rb.build();
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/wonderful").getHandlers()) == oneWild);
		assertTrue(Lo.last(router.lookup(HTTPMethods.GET, "/wonderful").getHandlers()) == oneWildSecondHandler);

		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/literal").getHandlers()) == oneLiteral);
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.POST, "/sdf").getHandlers()) == oneWildDifferentVerb);
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/a/loc/to").getHandlers()) == stuffThenWild);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/a/to/be").getHandlers()) == wildThenStuff);
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/wee/to/be").getHandlers()) == wildThenStuff);
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.POST, "/re/collect").getHandlers()) == collection);
		
		assertTrue(Lo.first(router.lookup(HTTPMethods.GET, "/a/loc/here/ok").getHandlers()) == surround);
	}
	
	@Test
	public void testLateFailNoCatchall() {
		RouterBuilder rb = new RouterBuilder();
		
		EmptyFactory late = new EmptyFactory();
		rb.post("/a/:really/late/fail", late);
		
		IRouter router = rb.build();
		
		assertTrue(router.lookup(HTTPMethods.POST, "/a/sdf/late/fail/now") == null);
	}
	
	@Test
	public void testLateFailWithCatchall() {
		RouterBuilder rb = new RouterBuilder();
		EmptyFactory caught = new EmptyFactory();
		rb.post("/different/late/fail/**", caught);
		IRouter router = rb.build();
		assertTrue(Lo.first(router.lookup(HTTPMethods.POST, "/different/late/fail/or/is/it").getHandlers()) == caught);
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
