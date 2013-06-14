package com.tantaman.ferox.api;

import io.netty.handler.codec.http.HttpContent;

public interface IHttpContent extends IHttpReception {
	public HttpContent getRaw();
	public boolean isLast();
}
