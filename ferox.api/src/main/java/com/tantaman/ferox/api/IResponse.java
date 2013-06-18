package com.tantaman.ferox.api;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpResponseStatus;


public interface IResponse {
	public void add(Object msg);
	public ChannelFuture write();
	public ChannelFuture writePartial();
	
	public void send(String response, HttpResponseStatus status);
	public void send(String response, String contentType, HttpResponseStatus status);
	public void send(Object response, String contentType, HttpResponseStatus status);
	public void send(Object response, HttpResponseStatus status);
}
