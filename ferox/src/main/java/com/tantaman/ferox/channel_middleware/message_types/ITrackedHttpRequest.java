package com.tantaman.ferox.channel_middleware.message_types;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.List;
import java.util.Map;

import com.tantaman.ferox.api.IDisposable;

public interface ITrackedHttpRequest {
	public HttpRequest getRawRequest();
	public Map<String, Attribute> body();
	public List<FileUpload> files();
	public void addDisposable(IDisposable disposable);
	public void dispose();
}
