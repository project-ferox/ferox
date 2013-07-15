package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpResponseStatus;

public interface IFineGrainedResponse {
	public void addResponseHeaders();
	public void addResponseHeaders(HttpResponseStatus status);
	public void add(Object msg);
	
	public ChannelFuture write();
	public ChannelFuture writePartial();
	public void close();
}
