package com.tantaman.ferox.priv;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpRequest;
import com.tantaman.ferox.channel_middleware.message_types.TrackedHttpRequest;

public class HttpRequest extends HttpReception implements IHttpRequest {
	public HttpRequest(Map<String, String> urlParams,
			Map<String, List<String>> query,
			List<String> splats,
			String catchall,
			TrackedHttpRequest trackedRequest) {
		super(trackedRequest.getRawRequest(), urlParams, query, splats, catchall, trackedRequest);
	}
}
