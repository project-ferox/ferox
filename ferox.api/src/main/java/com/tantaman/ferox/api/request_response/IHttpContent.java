package com.tantaman.ferox.api.request_response;

import io.netty.buffer.ByteBuf;


public interface IHttpContent extends IHttpReception {
	public ByteBuf getContent();
}
