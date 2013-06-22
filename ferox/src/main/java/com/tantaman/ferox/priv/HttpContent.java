package com.tantaman.ferox.priv;

import io.netty.handler.codec.http.LastHttpContent;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpContent;

public class HttpContent extends HttpReception implements IHttpContent {
	private final io.netty.handler.codec.http.HttpContent rawContent;
	
	public HttpContent(io.netty.handler.codec.http.HttpContent content,
			Map<String, String> urlParams, Map<String, List<String>> query, List<String> splats, String catchall) {
		super(urlParams, query, splats, catchall);
		rawContent = content;
	}

	@Override
	public io.netty.handler.codec.http.HttpContent getRaw() {
		return rawContent;
	}
	
	@Override
	public boolean isLast() {
		return rawContent instanceof LastHttpContent;
	}
}
