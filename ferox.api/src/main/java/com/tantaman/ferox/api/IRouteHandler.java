package com.tantaman.ferox.api;


public interface IRouteHandler {
	public void request(IHttpRequest request, IResponse response, IRequestChainer next);
	public void content(IHttpContent content, IResponse response, IRequestChainer next);
}
