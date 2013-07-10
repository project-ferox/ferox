package com.tantaman.ferox.priv;

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
	
	public HttpReception(Map<String, String> urlParams,
						 Map<String, List<String>> query,
						 List<String> splats,
						 String catchall,
						 ITrackedHttpRequest trackedRequest) {
		urlParameters = urlParams;
		querystringParameters = query;
		this.splats = Collections.unmodifiableList(splats);
		this.catchall = catchall;
		this.trackedRequest = trackedRequest;
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
		if (trackedRequest != null)
			trackedRequest.dispose();
	}
}
