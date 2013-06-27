package com.tantaman.ferox.api.router;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.api.request_response.IRequestChainer;
import com.tantaman.ferox.api.request_response.IResponse;


public interface IRouteHandler {
	public void request(IHttpRequest request, IResponse response, IRequestChainer next);
	public void content(IHttpContent content, IResponse response, IRequestChainer next);
	public void lastContent(IHttpContent content, IResponse response, IRequestChainer next);
}
