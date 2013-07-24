package com.tantaman.ferox.api.router;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.api.request_response.IRequestChainer;
import com.tantaman.ferox.api.request_response.IResponse;

/**
 * Interface for users to implement when intercepting requests on a given route.<br/><br/>
 * 
 * <ul>
 * <li>request is called when an IHttpRequest is received on the route</li>
 * <li>content is called when content associated with the http request is received for the given route</li>
 * <li>lastContent is called when the final piece of content associated with the request has been received</li>
 * <li>exceptionCaught is called if an exception was thrown from an {@link IRouteHandler}</li>
 * </ul>
 * 
 * @author tantaman
 *
 */
public interface IRouteHandler {
	public void request(IHttpRequest request, IResponse response, IRequestChainer next);
	public void content(IHttpContent content, IResponse response, IRequestChainer next);
	public void lastContent(IHttpContent content, IResponse response, IRequestChainer next);
	public void exceptionCaught(Throwable cause, IResponse response, IRequestChainer next);
}
