package com.tantaman.ferox.api.request_response;

import io.netty.buffer.ByteBuf;

/**
 * Represents some piece of content of an HTTP request.
 * 
 * A single HTTPRequest will be accompanied by 1..n {@link IHttpContent}s.
 * 
 * @author tantaman
 *
 */
public interface IHttpContent extends IHttpReception {
	/**
	 * The raw bytes of the content.
	 * @return
	 */
	public ByteBuf getContent();
}
