package com.tantaman.ferox.priv;

import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.middleware.message_types.TrackedHttpRequest;

public class HttpContent extends HttpReception implements IHttpContent {
	private final io.netty.handler.codec.http.HttpContent content;
	public HttpContent(io.netty.handler.codec.http.HttpContent content,
			Map<String, String> urlParams,
			Map<String, List<String>> query,
			List<String> splats,
			String catchall,
			TrackedHttpRequest trackedRequest) {
		super(content, urlParams, query, splats, catchall, trackedRequest);
		this.content = content;
	}
	
	@Override
	public ByteBuf getContent() {
		return content.content();
	}
}
