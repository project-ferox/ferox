package com.tantaman.ferox.channel_middleware;

import io.netty.channel.ChannelHandler;

import com.tantaman.ferox.api.IChannelHandlerFactory;

public class ChannelMiddleware {
	public static final IChannelHandlerFactory BODY_PARSER = new IChannelHandlerFactory() {
		@Override
		public ChannelHandler create() {
			return new BodyParser();
		}
	};
	
//	public static final IChannelHandlerFactory CSRF = new IChannelHandlerFactory() {
//		@Override
//		public ChannelHandler create() {
//			return new CsrfHandler();
//		}
//	};
}
