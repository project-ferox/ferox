package com.tantaman.ferox.api.pluggable;

import com.tantaman.ferox.api.IChannelHandlerFactory;

/**
 * Creates a channel handler that is used in the "pluggable" form of Ferox.
 * @author tantaman
 *
 */
public interface IPluggableChannelHandlerFactory extends IChannelHandlerFactory {
	public String getIdentifier();
}
