package com.tantaman.ferox.api;

import io.netty.handler.codec.http.LastHttpContent;

public class RouteHandlerAdapter implements IRouteHandler {

	@Override
	public void request(IHttpRequest request, IResponse response,
			IRequestChainer next) {
		next.request(request);
	}

	@Override
	public void content(IHttpContent content, IResponse response,
			IRequestChainer next) {
		if (content instanceof LastHttpContent) {
			lastContent(content, response, next);
		}
		next.content(content);
	}
	
	public void lastContent(IHttpContent content, IResponse response, IRequestChainer next) {
		
	}

}
