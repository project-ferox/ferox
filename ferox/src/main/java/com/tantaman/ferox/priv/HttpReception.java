package com.tantaman.ferox.priv;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.request_response.IHttpReception;

public class HttpReception implements IHttpReception {
	private final Map<String, String> urlParameters;
	private final Map<String, List<String>> querystringParameters;
	private final List<String> splats;
	private final String catchall;
	
	public HttpReception(Map<String, String> urlParams, Map<String, List<String>> query, List<String> splats, String catchall) {
		urlParameters = urlParams;
		querystringParameters = query;
		this.splats = Collections.unmodifiableList(splats);
		this.catchall = catchall;
	}
	
	public String getUrlParam(String key) {
		return urlParameters.get(key);
	}
	
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
}
