package com.tantaman.ferox.api.request_response;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.IDisposable;

public interface IHttpReception {
	public String getUrlParam(String key);
	public List<String> getQueryParam(String key);
	public List<String> getSplats();
	public String getCatchall();
	public HttpMethod getMethod();
	public boolean isLast();
	public HttpHeaders getHeaders();
	public String getUri();
	public Object getRaw();
	public List<FileUpload> getFiles();
	public Map<String, Attribute> getBody();
	public void addDisposable(IDisposable disposable);
	
	/**
	 * Only needs to be called on the last processed
	 * data and only if form data was received.
	 */
	public void dispose();
}
