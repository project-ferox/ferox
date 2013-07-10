package com.tantaman.ferox.api.request_response;

import java.util.List;

public interface IHttpReception {
	public String getUrlParam(String key);
	public List<String> getQueryParam(String key);
	public List<String> getSplats();
	public String getCatchall();
	
	/**
	 * Only needs to be called on the last processed
	 * data and only if form data was received.
	 */
	public void dispose();
}
