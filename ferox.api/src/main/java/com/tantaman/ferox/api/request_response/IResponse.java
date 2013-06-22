package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;


public interface IResponse {
	public void add(Object msg);
	
	public ChannelFuture write();
	public ChannelFuture writePartial();
	
	public IHeaderBuilder getHeaderBuilder();
	public HttpHeaders getHeaders();
	
	public void send(String response);	// assumes html
	public void send(String response, String contentType);
	public void send(String response, HttpResponseStatus status);
	public void send(String response, String contentType, HttpResponseStatus status);
	
	public void send(Object response);	// assumes json
	public void send(Object response, String contentType);
	public void send(Object response, HttpResponseStatus status);
	public void send(Object response, String contentType, HttpResponseStatus status);
}
