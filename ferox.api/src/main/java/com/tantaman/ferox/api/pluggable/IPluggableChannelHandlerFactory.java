package com.tantaman.ferox.api.pluggable;

import com.tantaman.ferox.api.IChannelHandlerFactory;

public interface IPluggableChannelHandlerFactory extends IChannelHandlerFactory {
	public String getIdentifier();
}
