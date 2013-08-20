package com.tantaman.ferox.channel_middleware;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Set;

import com.tantaman.ferox.channel_middleware.message_types.TrackedHttpRequest;

public class CookieHandler extends SimpleChannelInboundHandler<TrackedHttpRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext arg0,
			TrackedHttpRequest req) throws Exception {
		HttpRequest request = req.getRawRequest();
		String cookieString = request.headers().get(HttpHeaders.Names.COOKIE);
		if (cookieString != null) {
			Set<Cookie> cookies = CookieDecoder.decode(cookieString);
			req.setCookies(cookies);
		}
	}
}
