package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.concurrent.EventExecutor;

import java.util.Map;


public interface IResponse {
	public EventExecutor executor();
	
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
	
	public IFineGrainedResponse fineGrained();

	public ChannelFuture redirect(String uri, Map<String, String> queryParams);
}
