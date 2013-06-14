package com.tantaman.ferox.api;


public interface IRequestChainer {
	public void request(IHttpRequest request);
	public void content(IHttpContent content);
}
