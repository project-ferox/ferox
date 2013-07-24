package com.tantaman.ferox.api;

import io.netty.channel.ChannelHandler;

/**
 * Factory to create {@link ChannelHandler}s for use by Netty.
 * @author tantaman
 *
 */
public interface IChannelHandlerFactory {
	public ChannelHandler create();
}
