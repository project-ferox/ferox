package com.tantaman.ferox.api;

import io.netty.channel.ChannelHandler;

public interface IChannelHandlerFactory {
	public ChannelHandler create();
}
