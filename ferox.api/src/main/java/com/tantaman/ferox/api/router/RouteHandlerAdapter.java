package com.tantaman.ferox.api.router;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.api.request_response.IRequestChainer;
import com.tantaman.ferox.api.request_response.IResponse;


public class RouteHandlerAdapter implements IRouteHandler {

	@Override
	public void request(IHttpRequest request, IResponse response,
			IRequestChainer next) {
		next.request(request);
	}

	@Override
	public void content(IHttpContent content, IResponse response,
			IRequestChainer next) {
		next.content(content);
	}
	
	@Override
	public void lastContent(IHttpContent content, IResponse response, IRequestChainer next) {
		next.lastContent(content);
	}
	
	@Override
	public void exceptionCaught(Throwable cause, IResponse response,
			IRequestChainer next) {
		next.exceptionCaught(cause);
	}
}
