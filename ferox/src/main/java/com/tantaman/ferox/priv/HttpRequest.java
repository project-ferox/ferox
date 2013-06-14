package com.tantaman.ferox.priv;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.IHttpRequest;

public class HttpRequest extends HttpReception implements IHttpRequest {
	private final io.netty.handler.codec.http.HttpRequest rawRequest;
	
	public HttpRequest(
			io.netty.handler.codec.http.HttpRequest rawRequest,
			Map<String, String> urlParams, Map<String, List<String>> query, List<String> splats, String catchall) {
		super(urlParams, query, splats, catchall);
		this.rawRequest = rawRequest;
	}

	@Override
	public io.netty.handler.codec.http.HttpRequest getRaw() {
		return this.rawRequest;
	}
}
