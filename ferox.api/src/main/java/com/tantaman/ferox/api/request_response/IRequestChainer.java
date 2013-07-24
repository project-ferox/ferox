package com.tantaman.ferox.api.request_response;

import com.tantaman.ferox.api.router.IRouteHandler;

/**
 * Methods to invoke the next {@link IRouteHandler} in the route pipeline.
 * @author tantaman
 *
 */
public interface IRequestChainer {
	/**
	 * Invoke the next {@link IRouteHandler}'s <code>request</code> method.
	 * @param request
	 */
	public void request(IHttpRequest request);
	/**
	 * Invoke the next {@link IRouteHandler}'s <code>content</code> method.
	 * @param request
	 */
	public void content(IHttpContent content);
	/**
	 * Invoke the next {@link IRouteHandler}'s <code>lastContent</code> method.
	 * @param request
	 */
	public void lastContent(IHttpContent content);
	/**
	 * Invoke the next {@link IRouteHandler}'s <code>exceptionCaught</code> method.
	 * @param request
	 */
	public void exceptionCaught(Throwable cause);
}
