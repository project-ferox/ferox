package com.tantaman.ferox.api;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpResponseStatus;


public interface IResponse {
	public void add(Object msg);
	public ChannelFuture write();
	public ChannelFuture writePartial();
	
	public void send(String stringResponse, HttpResponseStatus status);
	public void send(Object jsonResponse, IJsonSerializer serializer, HttpResponseStatus status);
	public void send(String response, String contentType, HttpResponseStatus status);
}
