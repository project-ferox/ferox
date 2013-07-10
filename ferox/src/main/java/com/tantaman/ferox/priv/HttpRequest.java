package com.tantaman.ferox.priv;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.middleware.message_types.TrackedHttpRequest;

public class HttpRequest extends HttpReception implements IHttpRequest {
	private final io.netty.handler.codec.http.HttpRequest rawRequest;
	
	public HttpRequest(Map<String, String> urlParams,
			Map<String, List<String>> query,
			List<String> splats,
			String catchall,
			TrackedHttpRequest trackedRequest) {
		super(urlParams, query, splats, catchall, trackedRequest);
		this.rawRequest = trackedRequest.getRawRequest();
	}

	@Override
	public io.netty.handler.codec.http.HttpRequest getRaw() {
		return this.rawRequest;
	}
}
