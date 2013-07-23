package com.tantaman.ferox.api;

import io.netty.channel.ChannelHandler;

/**
 * 
 * @author tantaman
 *
 */
public interface IChannelHandlerFactory {
	public ChannelHandler create();
}
