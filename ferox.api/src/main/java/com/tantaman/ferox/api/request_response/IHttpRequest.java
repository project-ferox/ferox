package com.tantaman.ferox.api.request_response;

import io.netty.handler.codec.http.HttpRequest;

public interface IHttpRequest extends IHttpReception {
	public HttpRequest getRaw();
}
