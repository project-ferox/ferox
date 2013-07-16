package com.tantaman.ferox.priv;

import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpContent;
import com.tantaman.ferox.channel_middleware.message_types.TrackedHttpRequest;

public class HttpContent extends HttpReception implements IHttpContent {
	private final io.netty.handler.codec.http.HttpContent content;
	public HttpContent(io.netty.handler.codec.http.HttpContent content,
			String path,
			Map<String, String> urlParams,
			Map<String, List<String>> query,
			List<String> splats,
			String catchall,
			TrackedHttpRequest trackedRequest) {
		super(content, path, urlParams, query, splats, catchall, trackedRequest);
		this.content = content;
	}
	
	@Override
	public ByteBuf getContent() {
		return content.content();
	}
}
