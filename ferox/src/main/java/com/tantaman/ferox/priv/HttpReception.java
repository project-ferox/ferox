package com.tantaman.ferox.priv;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.LastHttpContent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpReception;
import com.tantaman.ferox.middleware.message_types.ITrackedHttpRequest;

public class HttpReception implements IHttpReception {
	private final Map<String, String> urlParameters;
	private final Map<String, List<String>> querystringParameters;
	private final List<String> splats;
	private final String catchall;
	private final ITrackedHttpRequest trackedRequest;
	private final Object rawContent;
	
	public HttpReception(Object raw, Map<String, String> urlParams,
						 Map<String, List<String>> query,
						 List<String> splats,
						 String catchall,
						 ITrackedHttpRequest trackedRequest) {
		urlParameters = urlParams;
		querystringParameters = query;
		this.splats = Collections.unmodifiableList(splats);
		this.catchall = catchall;
		this.trackedRequest = trackedRequest;
		rawContent = raw;
	}
	
	@Override
	public HttpMethod getMethod() {
		return trackedRequest.getRawRequest().getMethod();
	}
	
	public String getUrlParam(String key) {
		return urlParameters.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getQueryParam(String key) {
		List<String> param = querystringParameters.get(key);
		
		return param == null ? Collections.EMPTY_LIST : param;
	}
	
	public List<String> getSplats() {
		return splats;
	}
	
	public String getCatchall() {
		return catchall;
	}
	
	@Override
	public void dispose() {
		trackedRequest.dispose();
	}
	
	public boolean isLast() {
		return rawContent instanceof LastHttpContent;
	}
	
	@Override
	public String getUri() {
		return trackedRequest.getRawRequest().getUri();
	}
	
	@Override
	public HttpHeaders getHeaders() {
		return trackedRequest.getRawRequest().headers();
	}
}
