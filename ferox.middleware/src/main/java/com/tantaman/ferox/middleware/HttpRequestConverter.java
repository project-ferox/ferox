package com.tantaman.ferox.middleware;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;

import com.tantaman.ferox.middleware.message_types.TrackedHttpRequest;

@Sharable
public class HttpRequestConverter extends MessageToMessageDecoder<HttpRequest> {
	@Override
	protected void decode(ChannelHandlerContext ctx, HttpRequest msg,
			MessageList<Object> out) throws Exception {
		out.add(new TrackedHttpRequest(msg));
	}
}
