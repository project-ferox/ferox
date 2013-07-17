package com.tantaman.ferox.api.request_response;



public interface IRequestChainer {
	public void request(IHttpRequest request);
	public void content(IHttpContent content);
	public void lastContent(IHttpContent content);
	public void exceptionCaught(Throwable cause);
}
