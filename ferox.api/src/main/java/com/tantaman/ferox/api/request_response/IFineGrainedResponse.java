package com.tantaman.ferox.api.request_response;

import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Interface that provides more detailed control over 
 * a route handler's response to a request.
 * 
 * @author tantaman
 *
 */
public interface IFineGrainedResponse {
	/**
	 * A the headers that have been configured via <code>response.headers().set(...)</code>
	 * to the outgoing message queue.
	 */
	public void addResponseHeaders();
	
	/**
	 * Add the headers that have been configured via <code>response.headers().set(...)</code>
	 * to the outgoing message queue but with the given response status.
	 * @param status The response status (200, 400, etc.)
	 */
	public void addResponseHeaders(HttpResponseStatus status);
	
	/**
	 * Add an object to the outgoing message queue
	 * @param msg Object to write as a part of the response
	 */
	public void add(Object msg);
	
	/**
	 * Write the message queue to the channel.  No further write calls may be made after invoking this one.
	 * To make many write calls use writePartial.
	 * @return A future representing the write action
	 */
	public ChannelFuture write();
	
	/**
	 * Write the message queue to the channel.  This may be callled
	 * n times.
	 * 
	 * e.g.:
	 * <code>
	 * response.fineGrained().addResponseHeaders();
	 * response.fineGrained().add(pushContent);
	 * response.fineGrained().writePartial();
	 * ...
	 * response.fineGrained().add(pushContent2);
	 * response.fineGrained().writePartial();
	 * </code>
	 * 
	 * Although the final write by a handler should be a call to
	 * <code>write</code> instead of <code>writePartial</code>.
	 * @return
	 */
	public ChannelFuture writePartial();
	
	/**
	 * Close the connection.  Clients don't normally need to worry about invoking this method.
	 * This method will be called by <code>write()</code> if the connection requires it.
	 */
	public void close();
}
