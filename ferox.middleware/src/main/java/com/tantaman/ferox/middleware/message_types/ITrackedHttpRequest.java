package com.tantaman.ferox.middleware.message_types;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;

public interface ITrackedHttpRequest {
	public HttpRequest getRawRequest();
	public Map<String, Attribute> body();
	public List<FileUpload> files();
	public void dispose();
}
