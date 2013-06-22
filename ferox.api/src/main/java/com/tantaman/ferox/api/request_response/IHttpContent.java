package com.tantaman.ferox.api.request_response;

import io.netty.handler.codec.http.HttpContent;

public interface IHttpContent extends IHttpReception {
	public HttpContent getRaw();
	public boolean isLast();
}
