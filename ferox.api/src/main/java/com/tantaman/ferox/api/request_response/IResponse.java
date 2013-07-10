package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;


public interface IResponse {
	public void add(Object msg);
	
	public ChannelFuture write();
	public ChannelFuture writePartial();
	
	public HttpHeaders headers();
	
	public ChannelFuture send(String response);	// assumes html
	public ChannelFuture send(String response, String contentType);
	public ChannelFuture send(String response, HttpResponseStatus status);
	public ChannelFuture send(String response, String contentType, HttpResponseStatus status);
	
	public ChannelFuture send(Object response);	// assumes json
	public ChannelFuture send(Object response, String contentType);
	public ChannelFuture send(Object response, HttpResponseStatus status);
	public ChannelFuture send(Object response, String contentType, HttpResponseStatus status);
	
	public void setUserData(Object data);
	public <T> T getUserData();
}
