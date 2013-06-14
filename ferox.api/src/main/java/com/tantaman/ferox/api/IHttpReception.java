package com.tantaman.ferox.api;

import java.util.List;

public interface IHttpReception {
	public String getUrlParam(String key);
	public List<String> getQueryParam(String key);
	public List<String> getSplats();
	public String getCatchall();
}
